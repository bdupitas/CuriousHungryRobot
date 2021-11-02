package com.bdupitas.CHR;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;

//The physical state of the robot and physical battery
public class Robot implements CuriousHungryRobot {
    private Energy robotBattery; //holds the current energy level of Henry; however this will serve as a way to access energy
    private Point gps;
    private double mileage; // the distance traveled
    private EnergyField field;
    private Deque<Energy> memory;
    private String state = "";

    Energy hungryGoal;
    Energy curiousGoal;


    public Robot() {//initializer
        field = new EnergyField();
        robotBattery = new Energy((int) ROBOT_ENERGY_CAPACITY);
        gps = new Point(0, 0);
        memory = new ArrayDeque<>();
        mileage = 0;
        checkState();
    }

    public double getEnergy() {
        return robotBattery.checkBattery();
    }

    public double getMileage() {
        return this.mileage;
    }

    //movements
    protected void whereAmI() { //returns location on x,y
        System.out.println(this.gps.getLocation());
    }

    public double currentX() {
        return this.gps.getX();
    }

    public double currentY() {
        return this.gps.getY();
    }

    public Point currentLocation() {
        return this.gps;
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

    public void setRobX(double x) { //sets the x value after a snap distance
        this.gps.setLocation(x, gps.getY());
    }

    public void setRobY(double y) {
        this.gps.setLocation(gps.getX(), y);

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

        return;

    }

    public String checkState() {
        if (robotBattery.checkBattery() > ROBOT_ENERGY_CAPACITY / 2) {
            this.state = "curious";
        } else if (robotBattery.checkBattery() > 0) {
            this.state = "hungry";
        }
        return state;
    }

    public void getHungryGoal(int simulationRun) {
        if (!memory.isEmpty()) {
            if (simulationRun == 0) {
                // stack
                hungryGoal = memory.removeFirst(); //.pop()
            } else if (simulationRun == 1) {
                hungryGoal = memory.removeFirst();
            }

        } else if (memory.isEmpty()) {
            step(curiousGoal); // no memory curious walk.
        }

    }

    private void setState(String state) {
        this.state = state;
    }

    public void goalWalk(int simNumber) {
        // if hungry, and no goal, curious walk.
        if (state == "curious") {
           // System.out.println("I am curious");
            if (curiousGoal == null) {
                getCuriousGoal();
            } else if (goalReached(curiousGoal)) {
               // System.out.printf("Curious Goal Reached. Current Battery %f\n", robotBattery.checkBattery());
                getCuriousGoal();
            } else {

                step(curiousGoal);
            }

        } else if (state == "hungry") {
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

        checkState();
        detectEnergy(simNumber);
    }


    private void curiousHungryWalk() {

    }

    private void detectEnergy(int simNumber) {
        Energy nearestEnergy = detect(); // this locates nearest energy
        if (nearestEnergy == null) {
            return;
        } else if (!nearestEnergy.hasVisited() && !memory.contains(nearestEnergy)){
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
        boolean xGoal = false, yGoal = false;

        //goal location
        double gPointx = goal.getX();
        double gPointy = goal.getY();

        //current location
        double cPointx = this.currentX();
        double cPointy = this.currentY();

        if (gPointx == cPointx && gPointy == cPointy) {
            getCuriousGoal();
        }

        //if any of these come back true this will inform the code below if a snap will happen, if true then this robot will 'snap' towards whichever location
        boolean Snap = checkSnap(gPointx, gPointy);

        // if by any chance the value of the xValue or yValue is equal to the goal the robot will attempt to conserve energy and travel no,we,ea,so.
        if (cPointx == gPointx) {
            xGoal = true;
        }
        if (cPointy == gPointx) {
            yGoal = true;

        }

        // these points will also remind Henry that once a point is achieved, to start heading and getting the point
        if (cPointx < gPointx && cPointy < gPointy && !xGoal && !yGoal) {//North-east
            stepNorthEast();
            return;
        }
        if (cPointx > gPointx && cPointy < gPointy && !xGoal && !yGoal) { //NorthWest
            stepNorthWest();
            return;
        }
        if (cPointx < gPointx && cPointy > gPointy && !xGoal && !yGoal) {// southEast
            stepSouthEast();
            return;
        }
        if (cPointx > gPointx && cPointy > gPointy && !xGoal && !yGoal) {//Southwest
            stepSouthWest();
            return;
        }
        // when diagonals are not optimal
        if (cPointx < gPointx) { //east
            moveEast();
            return;
        }
        if (cPointx > gPointx) { // west
            moveWest();
            return;
        }
        if (cPointy < gPointy) { // north
            moveNorth();
            return;
        }
        if (cPointy > gPointy) { //west
            moveSouth();
            return;
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