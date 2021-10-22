package com.bdupitas.CHR;

import java.awt.*;
import java.util.ArrayList;


public class EnergyField implements CuriousHungryRobot {
    private ArrayList<Energy> energyPoint;

    EnergyField() {
        energyPoint = new ArrayList<>();
        for (int i = 0; i < numberOfEnergyHubs; i++) {
            Energy temp = new Energy(randomCoordinate(),randomCoordinate());
            energyPoint.add(temp);
        }
        verifyIntervals();
    }

    private void verifyIntervals() { //change it to a nested for
        boolean badpoint = true;
        do {
            for (int i = 0; i < energyPoint.size() - 1; i++) {
                Energy point1 = energyPoint.get(i); //first point
                double x = point1.getX();
                double y = point1.getY();
                for (int k = i + 1; k < energyPoint.size(); k++) {
                    Energy point2 = energyPoint.get(k);
                    double cx = point2.getX();
                    double cy = point2.getY();
                    double distance = distancePts(x, y, cx, cy);
                    if (distance < 20) {// +- energy interface distance
                        point2.setLocation(randomCoordinate(), randomCoordinate());
                        verifyIntervals();
                    } else {
                        badpoint = false;
                    }

                }
            }
        } while (badpoint);
    }

    protected int randomCoordinate() {
        return (int) ((Math.random() * 401) - 200);
    }

     double distancePts(double X1, double Y1, double X2, double Y2) {

        return Math.sqrt(Math.pow((X2 - X1), 2) + Math.pow((Y2 - Y1), 2));
    }
     double distancePoints(Point A, Point B){
        double X1 = A.getX();
        double Y1 = A.getY();
        double X2 = B.getX();
        double Y2 = B.getY();

        return Math.sqrt(Math.pow((X2 - X1), 2) + Math.pow((Y2 - Y1), 2));
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
