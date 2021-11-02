package com.bdupitas.CHR;

import java.awt.*;
import java.util.ArrayList;


public class EnergyField implements CuriousHungryRobot {
    private ArrayList<Energy> energyPoint;

    EnergyField() {
        energyPoint = new ArrayList<>();
        for (int i = 0; i < numberOfEnergyHubs; i++) {
            Energy temp = new Energy(randomCoordinate(), randomCoordinate());
            energyPoint.add(temp);
        }
        verifyIntervals();
    }

    private void verifyIntervals() { //change it to a nested for
        boolean badPointExists = true;
        do {
            for (int i = 0; i < energyPoint.size() - 1; i++) {
                Energy point1 = energyPoint.get(i); //first point
                for (int k = i + 1; k < energyPoint.size(); k++) {
                    Energy point2 = energyPoint.get(k);
                    if (distancePoints(point1.getLocation(), point2.getLocation()) < energyIntervals) {// +- energy interface distance
                        reAdjustLocation(point1, point2);
                        break;
                    } else {
                        badPointExists = false;
                    }

                }
            }
        } while (badPointExists);
    }

    private void reAdjustLocation(Energy hub1, Energy hub2) {
        while (distancePoints(hub1.getLocation(), hub2.getLocation()) < energyIntervals) {
            hub2.setLocation(randomCoordinate(), randomCoordinate());
        }
    }

    protected int randomCoordinate() {
        return (int) ((Math.random() * 401) - 200);
    }

    double distancePoints(Point A, Point B) {
        double X1 = A.getX();
        double Y1 = A.getY();
        double X2 = B.getX();
        double Y2 = B.getY();

        return Math.hypot((X2 - X1), (Y2 - Y1));

    }

    public Energy ping(Point currentLocation) { //returns the location of a point that is close
        Energy energyHub;
        for (int i = 0; i < energyPoint.size(); i++) {
            energyHub = energyPoint.get(i);


            double distance = distancePoints(currentLocation, energyHub.getLocation());


            if (distance <= 13) {
                return energyHub;
            }

        }

        return null;
    }

}
