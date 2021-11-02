package com.bdupitas.CHR;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

//The physical state of the robot and physical battery
final public class Robot implements CuriousHungryRobot {
    private final Energy robotBattery; //holds the current energy level of Henry; however this will serve as a way to access energy
    private final Point gps;
    private double mileage; // the distance traveled
    private final EnergyField field;
    private final Deque<Energy> memory;
    private String state = "";

    Energy hungryGoal;
    Energy curiousGoal;


    public Robot() {//initializer
        field = new EnergyField();
        robotBattery = new Energy((int) ROBOT_ENERGY_CAPACITY);
        gps = new Point(0, 0);
        memory = new ArrayDeque<>();
        mileage = 0;
        getState();
    }

    public double getEnergy() {
        return robotBattery.checkBattery();
    }

    public double getMileage() {
        return this.mileage;
    }

    public double currentX() {
        return this.gps.getX();
    }

    public double currentY() {
        return this.gps.getY();
    }


    public void setLocation(double X2, double Y2) {
        this.gps.setLocation(X2, Y2);

    }

    protected void moveNorth() { //north   +Y
        movement(REGULAR_MOVEMENT); // drainenergy
        this.gps.setLocation(this.gps.getX(), this.gps.getY() + REGULAR_MOVEMENT);
    }

    protected void moveSouth() { //move south  -Y
        movement(REGULAR_MOVEMENT); // drainenergy
        this.gps.setLocation(this.gps.getX(), this.gps.getY() - REGULAR_MOVEMENT);
    }

    protected void moveWest() { //moves west -x
        movement(REGULAR_MOVEMENT);
        this.gps.setLocation(this.gps.getX() - REGULAR_MOVEMENT, this.gps.getY());
    }

    protected void moveEast() { //moves east +x
        movement(REGULAR_MOVEMENT);
        this.gps.setLocation(this.gps.getX() + REGULAR_MOVEMENT, this.gps.getY());
    }

    protected void snapStraight(double x) {
        movement(x);
    }

    protected void stepNorthEast() { //DIAGONAL Northeast  +x +y
        movement(DIAGONAL_MOVEMENT);
        this.gps.setLocation(this.gps.getX() + REGULAR_MOVEMENT, this.gps.getY() + REGULAR_MOVEMENT);
    }

    protected void stepNorthWest() { //DIAGONAL Northwest -x +y
        movement(DIAGONAL_MOVEMENT);
        this.gps.setLocation(this.gps.getX() - REGULAR_MOVEMENT, this.gps.getY() + REGULAR_MOVEMENT);
    }

    protected void stepSouthWest() { //DIAGONAL southWest -x -y
        movement(DIAGONAL_MOVEMENT);
        this.gps.setLocation(this.gps.getX() - REGULAR_MOVEMENT, this.gps.getY() - REGULAR_MOVEMENT);
    }

    protected void stepSouthEast() { //DIAGONAL SouthEast  +x -y
        movement(DIAGONAL_MOVEMENT);
        this.gps.setLocation(this.gps.getX() + REGULAR_MOVEMENT, this.gps.getY() - REGULAR_MOVEMENT);
    }

    protected void movement(double v) {
        this.mileage += v;
        this.robotBattery.DrainEnergy(v); //the state of power comes from energy, but is within the battery pack of the robot
    }


    protected Energy detect() {
        // return nearest energy
        return field.ping(this.gps);
    }

    protected void consumeEnergy(Energy hungryGoal) { // energy consumed is only for hungry goals


        double leftover = hungryGoal.getCharge() + this.robotBattery.checkBattery() - ROBOT_ENERGY_CAPACITY;

        if (leftover > 0) {
            this.robotBattery.recharge(ROBOT_ENERGY_CAPACITY);
            hungryGoal.setCharge(leftover);
        } else {
            this.robotBattery.recharge(this.robotBattery.checkBattery() + hungryGoal.getCharge());
            hungryGoal.deplete();
            hungryGoal.visited();
        }

    }

    public void getState() {
        if (robotBattery.checkBattery() > ROBOT_ENERGY_CAPACITY / 2) {
            this.state = "curious";
        } else if (robotBattery.checkBattery() > 0) {
            this.state = "hungry";
        }
    }

    public void getHungryGoal(int simulationRun) {
        if (!memory.isEmpty()) {
            if (simulationRun == 0) {
                // stack
                hungryGoal = memory.removeFirst(); //.pop()
            } else if (simulationRun == 1) {
                hungryGoal = memory.removeFirst();
            }

        } else {
            step(curiousGoal); // no memory curious walk.
        }

    }

    public void goalWalk(int simNumber) {
        // if hungry, and no goal, curious walk.
        if (Objects.equals(state, "curious")) {
            // System.out.println("I am curious");
            if (curiousGoal == null) {
                getCuriousGoal();
            } else if (goalReached(curiousGoal)) {
                // System.out.printf("Curious Goal Reached. Current Battery %f\n", robotBattery.checkBattery());
                getCuriousGoal();
            } else {

                step(curiousGoal);
            }

        } else if (Objects.equals(state, "hungry")) {
            // System.out.println("I am Hungry");
            if (hungryGoal == null) {
                getHungryGoal(simNumber);

            } else if (goalReached(hungryGoal)) {
                //  System.out.printf("I found a goal nom nom nom.2");
                consumeEnergy(hungryGoal);
                hungryGoal = null;
            } else {

                step(hungryGoal);
            }
        }
        getState();
        detectEnergy(simNumber);
    }


    private void detectEnergy(int simNumber) {
        Energy nearestEnergy = detect(); // this locates nearest energy

        if (nearestEnergy != null && !nearestEnergy.hasVisited() && !memory.contains(nearestEnergy)) {
            if (simNumber == 0) { //stackMemory
                memory.addFirst(nearestEnergy); //.push(e)
                // nearestEnergy.visited();

            } else if (simNumber == 1) { //queueMemoryStyle
                memory.addLast(nearestEnergy);
                // nearestEnergy.visited();
            }
        }
    }


    private void step(Energy goal) {

        //goal location
        double goalX = goal.getX();
        double goalY = goal.getY();

        //current location
        double currentX = this.currentX();
        double currentY = this.currentY();

        if (goal.getLocation().equals(this.gps)) {
            getCuriousGoal();
        }

        //if any of these come back true this will inform the code below if a snap will happen, if true then this robot will 'snap' towards whichever location
        boolean Snap = checkSnap(goalX, goalY);
        if (!Snap) {
            //check if the robot is on the same plane, if so, the robot will travel along the plane, heading north, south, east or west
            if (!(currentX == goalX) && !(currentY == goalY)) {
                if (currentX < goalX && currentY < goalY) {//North-east
                    stepNorthEast();

                }
                if (currentX > goalX && currentY < goalY) { //NorthWest
                    stepNorthWest();

                }
                if (currentX < goalX && currentY > goalY) {// southEast
                    stepSouthEast();

                }
                if (currentX > goalX && currentY > goalY) {//Southwest
                    stepSouthWest();

                }

            }
            // when diagonals are not optimal
            else {
                if (currentX < goalX) { //east
                    moveEast();
                }
                if (currentX > goalX) { // west
                    moveWest();
                }
                if (currentY < goalY) { // north
                    moveNorth();
                }
                if (currentY > goalY) { //south
                    moveSouth();
                }
            }

        }

    }

    private boolean checkSnap(double X2, double Y2) { //method that detects snaps
        double X1 = this.gps.getX();
        double Y1 = this.gps.getY();
        int distance = (int) distancePts(X1, Y1, X2, Y2); // maybe change back to double
        if (distance <= SNAP_DISTANCE) {
            movement(distance);//costs movement
            setLocation(X2, Y2);
            return true;
        }
        return false;
    }

    double distancePts(double X1, double Y1, double X2, double Y2) {
        return Math.sqrt(Math.pow((X2 - X1), 2) + Math.pow((Y2 - Y1), 2));
    }

    private boolean goalReached(Energy currentGoal) { //determines if goal has been reached
        if (currentGoal == null) {
            return false;
        }
        return this.gps.getX() == currentGoal.getX() && this.gps.getY() == currentGoal.getY();

    }

    private void getCuriousGoal() {
        this.curiousGoal = new Energy((int) getRandomCoordinate(), (int) getRandomCoordinate());
    }

    double getRandomCoordinate() { //generates a random coordinate in int
        return (Math.random() * 401) - 200;
    }
}