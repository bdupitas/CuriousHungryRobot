package com.bdupitas.CHR;//the all knowing energy that exists in this virtual simulation

import java.awt.Point;
import java.util.*;

public class Energy implements CuriousHungryRobot {
    private double rEnergy;//robot has energy, this is where the energy comes from
    private double charge; //the energy hub energy
    protected Energy[] energyHub; // holds obj
    private List<Energy> energypoint; //holds energy point
    private Point hub; // energy stores
    private boolean reached; // this will hold values if reached or not before, this will prevent a point from being redetected and stored

    Energy() { // and the energy hub
        this.charge = energyInitialCapacity;
        this.hub = new Point(); // point
        this.hub.setLocation(genRandcoord(), genRandcoord());
        this.reached = false;
    }

    Energy(double x) {
        this.rEnergy = x; //initilizes robot Energy
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
        this.rEnergy = x;

    }

    protected boolean isReached() {
        return reached;
    }

    public void setReached(boolean x) {
        this.reached = x;
    }

    protected double checkBattery() { //returns robot energy level
        return this.rEnergy;
    }

    protected void DrainEnergy(double e) {//setter of movement
        double newE = this.rEnergy - e;
        this.rEnergy = newE;

    }

    protected double genRandcoord() { //generates a random coordinate
        double e = (Math.random() * 401) - 200;
        return e;
    }

    public double getX() {
        return this.hub.getX();

    }

    public double getY() {
        return this.hub.getY();

    }

    public void setLocation(double x, double y) {
        this.hub.setLocation(x, y);

    }


    public void EnergyPoint() {
        Energy[] energyHub = new Energy[energyLocations]; //produces n charges
        this.energypoint = new ArrayList<>();

        for (Energy name : energyHub) {
            name = new Energy(); //assigns points
            energypoint.add(name); //addint these points after they have all been instantiated to the arraylist

        }
        verifyIntervals();
    }

    void verifyIntervals() { //change it to a nested for
        boolean badpoint = true;
        do {
            for (int i = 0; i < energypoint.size() - 1; i++) {
                Energy point1 = energypoint.get(i); //first
                double x = point1.getX();
                double y = point1.getY();
                for (int k = i + 1; k < energypoint.size(); k++) {
                    Energy point2 = energypoint.get(k);
                    double cx = point2.getX();
                    double cy = point2.getY();
                    double distance = distancePts(x, y, cx, cy);
                    if (distance < 20) {// +- energy interface distance
                        point2.setLocation(genRandcoord(), genRandcoord());
                        verifyIntervals();
                    } else {
                        badpoint = false;
                    }

                }
            }
        } while (badpoint);
    }

    protected double distancePts(double X1, double Y1, double X2, double Y2) {
        double distance = Math.sqrt(Math.pow((X2 - X1), 2) + Math.pow((Y2 - Y1), 2));
        return distance;
    }

    protected Energy ping(double rX, double rY) { //returns the location of a point that is close
        Energy eHub;
        List<Energy> temp = this.energypoint;
        for (int i = 0; i < energypoint.size(); i++) {
            eHub = energypoint.get(i);
            double X = (rX);
            double Y = (rY);
            double tX = (eHub.getX());
            double tY = (eHub.getY());
            double distance = distancePts(rX, rY, tX, tY);


            if (distance <= 13) {
                return eHub;
            }

        }

        return null;
    }
}