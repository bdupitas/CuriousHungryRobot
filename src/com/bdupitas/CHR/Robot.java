package com.bdupitas.CHR;

import java.awt.Point;

//The physical state of the robot and physical battery
public class Robot implements CuriousHungryRobot {
    private Energy robotBattery; //holds the current energy level of Henry; however this will serve as a way to access energy
    private Point gps;
    private double mileage; // the distance traveled
    private EnergyField field;



    public Robot() {//initializer
        field = new EnergyField();
        this.robotBattery = new Energy((int) robotEnergyCapacity);
        this.gps = new Point(0,0);

        this.mileage = 0;
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

    public Point currentLocation(){
        return this.gps;
    }

    public void setLocation(double X2, double Y2) {
        this.gps.setLocation(X2, Y2);

    }

    protected void moveNorth() { //north   +Y
        movement(eRegMovm); // drainenergy
        this.gps.setLocation(this.gps.getX(), this.gps.getY() + eRegMovm);
    }

    protected void moveSouth() { //move south  -Y
        movement(eRegMovm); // drainenergy
        this.gps.setLocation(this.gps.getX(), this.gps.getY() - eRegMovm);
    }

    protected void moveWest() { //moves west -x
        movement(eRegMovm);
        this.gps.setLocation(this.gps.getX() - eRegMovm, this.gps.getY());
    }

    protected void moveEast() { //moves east +x
        movement(eRegMovm);
        this.gps.setLocation(this.gps.getX() + eRegMovm, this.gps.getY());
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
        movement(diagMovm);
        this.gps.setLocation(this.gps.getX() + eRegMovm, this.gps.getY() + eRegMovm);
    }

    protected void stepNorthWest() { //DIAGONAL Northwest -x +y
        movement(diagMovm);
        this.gps.setLocation(this.gps.getX() - eRegMovm, this.gps.getY() + eRegMovm);
    }

    protected void stepSouthWest() { //DIAGONAL southWest -x -y
        movement(diagMovm);
        this.gps.setLocation(this.gps.getX() - eRegMovm, this.gps.getY() - eRegMovm);
    }

    protected void stepSouthEast() { //DIAGONAL SouthEast  +x -y
        movement(diagMovm);
        this.gps.setLocation(this.gps.getX() + eRegMovm, this.gps.getY() - eRegMovm);
    }

    protected void movement(double v) {
        this.mileage += v;
        this.robotBattery.DrainEnergy(v); //the state of power comes from energy, but is within the battery pack of the robot
    }

//    private Energy nearestGoal(){
//
//    }


    protected Energy detect() {    // ? is this a constructor?

        // return nearest energy
        Energy nearestEnergy = field.ping(this.gps.getX(), this.gps.getY());
        if (nearestEnergy != null) {
            return nearestEnergy;
        }
        return null;
    }

    protected void consumeEnergy(Energy hGoal) {
        Energy temp = hGoal;
        double charge = hGoal.getCharge();
        double currentBatt = this.robotBattery.checkBattery();
        if (charge + currentBatt > robotEnergyCapacity) {
            double leftover = charge + currentBatt - robotEnergyCapacity;
            temp.setCharge(leftover);
            this.robotBattery.recharge(robotEnergyCapacity);
            temp.setReached(false); // flips it back to false if there is leftover energies
        } else {
            this.robotBattery.recharge(currentBatt + charge);
            temp.deplete();
        }


    }
}