package com.bdupitas.CHR;

import java.awt.*;
import java.util.ArrayList;


public class EnergyField implements CuriousHungryRobot {
    private final ArrayList<Energy> energyPoint;

    EnergyField() {
        energyPoint = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ENERGY_HUBS; i++) {
            Energy temp = new Energy(randomCoordinate(), randomCoordinate());
            energyPoint.add(temp);
        }

        verifyIntervals();
    }

    private void verifyIntervals() { //continues until all the points are spaced out
        boolean badPointExists = true;
        do {
            for (int i = 0; i < energyPoint.size() - 1; i++) {
                Energy point1 = energyPoint.get(i); //first point
                for (int k = i + 1; k < energyPoint.size(); k++) {
                    Energy point2 = energyPoint.get(k);
                    if (distancePoints(point1.getLocation(), point2.getLocation()) < ENERGY_INTERVALS) {// +- energy interface distance
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
        while (distancePoints(hub1.getLocation(), hub2.getLocation()) < ENERGY_INTERVALS) {
            hub2.setLocation(randomCoordinate(), randomCoordinate());
        }
    }

    protected int randomCoordinate() {
        return (int) ((Math.random() * 401) - PLANE_ENERGY_RADIUS);
    }

    double distancePoints(Point A, Point B) {
        return Math.hypot((B.getX() - A.getX()), (B.getY() - A.getY()));
    }

    public Energy ping(Point currentLocation) { //returns the location of a point that is close
        Energy energyHub;
        for (int i = 0; i < energyPoint.size(); i++) {
            energyHub = energyPoint.get(i);
            double distance = distancePoints(currentLocation, energyHub.getLocation());
            if (distance <= ROBOT_ROBOT_DETECTION_RADIUS) {
                return energyHub;
            }

        }

        return null;
    }

}
