package com.bdupitas.CHR;//the all knowing energy that exists in this virtual simulation

import java.awt.Point;
import java.util.*;

public class Energy implements CuriousHungryRobot {
    private double robotBatteryLevel;//robot has energy, this is where the energy comes from
    private double charge; //the energy hub energy
    protected Energy[] energyLocations; // holds obj
    private List<Energy> energyPoint; //holds energy point
    private Point location; // energy stores
    private boolean reached; // this will hold values if reached or not before, this will prevent a point from being redetected and stored


    Energy(){
        this.charge = energyInitialCapacity;
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




//    public void initializeEnergyField() {
//        //  this.energyLocations = new Energy[numberOfEnergyHubs]; //produces n charges
    // this.energyPoint = new ArrayList<>();
//
//        for (Energy points : 33) {
//            points = new Energy(); // creations an energy points
//            energyPoint.add(points); //addint these points after they have all been instantiated to the arraylist
//        }
//        verifyIntervals();
//    }

//    Energy(double x) {
//        this.robotBatteryLevel = x; //initilizes robot Energy
//    }

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

    protected boolean isReached() {
        return reached;
    }

    public void setReached(boolean x) {
        this.reached = x;
    }

    protected double checkBattery() { //returns robot energy level
        return this.robotBatteryLevel;
    }

    protected void DrainEnergy(double e) {//setter of movement
        double newE = this.robotBatteryLevel - e;
        this.robotBatteryLevel = newE;

    }

    public double getX() {
        return this.location.getX();

    }

    public double getY() {
        return this.location.getY();

    }

    public void setLocation(double x, double y) {
        this.location.setLocation(x, y);

    }


//    protected Energy ping(double rX, double rY) { //returns the location of a point that is close
//        Energy eHub;
//        List<Energy> temp = this.energyPoint;
//        for (int i = 0; i < energyPoint.size(); i++) {
//            eHub = energyPoint.get(i);
//            double X = (rX);
//            double Y = (rY);
//            double tX = (eHub.getX());
//            double tY = (eHub.getY());
//            double distance = distancePts(rX, rY, tX, tY);
//            if (distance <= 13) {
//                return eHub;
//            }
//
//        }
//
//        return null;
//    }
}