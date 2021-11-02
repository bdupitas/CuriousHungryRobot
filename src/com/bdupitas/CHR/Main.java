
package com.bdupitas.CHR;//Mind and States of the Robot

public class Main implements CuriousHungryRobot {
    public static void main(String[] args) {
        int simulationRun = 0;// 0 - stack, 1 - queue

        while (simulationRun < 2) {
            Sample data = new Sample(); //allows for data to be held during this simulation
            int TrialNumber = 0; //up to 1000
            while (TrialNumber < TRIAL_LIMIT) {
                Robot rob1 = new Robot();

                do {
                    rob1.goalWalk(simulationRun);
                } while ((rob1.getEnergy()) > 0);
                inactive(rob1, data); //eventually have this report total movement to sample. sample to recurse
                TrialNumber++;

            }
            if (simulationRun == 0) {
                data.setName("Stack Memory");
            } else if (simulationRun == 1) {
                data.setName("Queue Memory");
            }

            data.computeStats();
            System.out.print(data.toString());


            simulationRun++;


        }
    }

    static void inactive(Robot Henry, Sample data) { // save statistics for this trial, increment to 1000, then recurse main
        double x = Henry.getMileage();
        data.addData(x);
    }
}
