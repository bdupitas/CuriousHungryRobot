package com.bdupitas.CHR;

import java.util.ArrayList;
import java.util.List;


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

    protected double distancePts(double X1, double Y1, double X2, double Y2) {
        return Math.sqrt(Math.pow((X2 - X1), 2) + Math.pow((Y2 - Y1), 2));
    }

    public Energy ping(double rX, double rY) { //returns the location of a point that is close
        Energy eHub;
        for (int i = 0; i < energyPoint.size(); i++) {
            eHub = energyPoint.get(i);
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
