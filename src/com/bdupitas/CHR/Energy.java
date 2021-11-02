package com.bdupitas.CHR;//the all-knowing energy that exists in this virtual simulation

import java.awt.Point;


public class Energy implements CuriousHungryRobot {
    private double robotBatteryLevel;//robot has energy, this is where the energy comes from
    private double charge; //the energy hub energy
    private Point location; // energy stores
    private boolean reached; // this will hold values if reached or not before, this will prevent a point from being redetected and stored


    Energy(){
        this.charge = ENERGY_INITIAL_CAPACITY;
        this.location = new Point(); // point
        this.reached = false;
    }

    Energy(int x, int y){
        this();
        this.location.setLocation(x,y);
    }

    Energy(int batteryLevel) { // constructor for robot battery
        this.robotBatteryLevel = batteryLevel;
    }


    public double getCharge() { //returns level of energy hub
        return this.charge;
    }

    public void setCharge(double x) { // if there will be left over;
        this.charge = x;
    }

    public void deplete() {
        this.charge = 0;
    }

    public void recharge(double x) {
        this.robotBatteryLevel = x;

    }

    protected boolean hasVisited() {
        return reached;
    }

    public void visited() {
        this.reached = true;
    }

    protected double checkBattery() { //returns robot energy level
        return this.robotBatteryLevel;
    }

    protected void DrainEnergy(double e) {//setter of movement
        this.robotBatteryLevel -= e;
    }

    public double getX() {
        return this.location.getX();

    }

    public double getY() {
        return this.location.getY();

    }

    public Point getLocation(){
        return location;
    }

    public void setLocation(double x, double y) {
        this.location.setLocation(x, y);

    }

}