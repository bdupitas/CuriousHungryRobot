
package com.bdupitas.CHR;//Mind and States of the Robot

import java.awt.Point;
import java.util.*;

public class Main extends Robot {
    private static boolean cState;

    public static void main(String args[]) {
        int simulationRun = 0;// 0 - stack, 1 - queue
        while (simulationRun < 2) {
            Sample data = new Sample(); //allows for data to be held during this simulation
            int trialNo = 0; //up to 1000
            while (trialNo < simulationTrial) {
                Robot rob1 = new Robot();
                Point curiousPoint = null, hungryPoint = null; //
                Energy hungerGoal = null;

                Deque<Energy> memory = new ArrayDeque<Energy>(); // Memory of Points

                final boolean stackMem = checkStack(simulationRun);// memory style
                final boolean queueMem = checkQueue(simulationRun);

                //all booleans set to false, as the robot will verify the energy before allowing
                boolean curiousState = false, hungryState = false, curiousGoal = false, hGoalExists = false, curiousGoalReached = false, hGoalReached = false;


                curiousState = checkCurious(rob1, curiousState);
                do {
                    if (curiousState) { //curious state

                        if (curiousGoal != true) {
                            curiousPoint = gerCuriousGoal();
                            curiousGoal = true;
                            curiousGoalReached = !curiousGoal;
                        } else if (curiousGoal && !curiousGoalReached) {
                            Gwalk(curiousPoint, rob1); //goal Walk
                            curiousGoalReached = goalReached(curiousPoint, rob1);
                            curiousGoal = !curiousGoalReached;

                        }

                    }
                    if (hungryState) { //hungry state

                        if (hGoalExists != true) {
                            hungerGoal = getHungryGoal(memory, stackMem, queueMem, curiousState, rob1);
                            if (hungerGoal == null) { //switch cases for this move
                                curiousState = true;
                                hungryState = false;
                                continue;
                            }
                            if (hungerGoal != null) {
                                hGoalExists = true;
                                hGoalReached = !hGoalExists;
                                int x = (int) (hungerGoal.getX());
                                int y = (int) (hungerGoal.getY());
                                hungryPoint = new Point(x, y);
                            }
                        } else if (hGoalExists && !hGoalReached) {
                            Gwalk(hungryPoint, rob1);
                            hGoalReached = goalReached(hungryPoint, rob1);


                        }
                        if (hGoalReached) {
                            rob1.consumeEnergy(hungerGoal);
                            hGoalExists = !hGoalReached;
                            hGoalReached = false; //once consumed a new goal should be set
                        }

                    }
                    curiousState = checkCurious(rob1, curiousState);
                    hungryState = checkHungry(rob1, hungryState);
                    detectEnergy(rob1, stackMem, queueMem, memory);
                }
                while ((rob1.getEnergy()) > 0);
                inactive(rob1, data); //eventually have this report total movement to sample. sample to recurse
                trialNo++;

            }
            if (simulationRun == 0) {
                data.setName("Stack Memory");
            } else if (simulationRun == 1) {
                data.setName("Queue Memory");
            }


            if (simulationRun != 2) {
                data.computeStats();
                System.out.printf(data.toString());

            }
            simulationRun++;
        }
    }

    static void inactive(Robot Henry, Sample data) { // save statistics for this trial, increment to 1000, then recurse main
        double x = Henry.getMileage();
        data.addData(x);
    }

    static boolean checkCurious(Robot Henry, boolean cState) { //checks if the robot is curious
        if (Henry.getEnergy() > (robotEnergyCapacity / 2)) {
            cState = true;
        } else {
            cState = false;
        }
        return cState;
    }


    static boolean checkHungry(Robot Henry, boolean hState) { // checks if the robot is hungry
        if ((Henry.getEnergy() <= (robotEnergyCapacity / 2)) && Henry.getEnergy() > 0) {
            hState = true;
        } else {
            hState = false;
        }
        return hState;
    }


    private static boolean goalReached(Point currentGoal, Robot rob1) { //determines if goal has been reached
        Point goal = currentGoal;
        Point currentLocation = new Point((int)rob1.currentX(), (int)rob1.currentY());

        return goal.equals(currentLocation);

    }

    private static Point gerCuriousGoal() { //curious goal.
        return new Point((int) genRandcoord(), (int) genRandcoord());
    }

    private static Energy getHungryGoal(Deque<Energy> memory, boolean stackMem, boolean queueMem, boolean cState, Robot Henry) { //returns a point of the memoryGoal
        Energy temp = null;
        if (stackMem) {
            try {
                temp = memory.removeFirst(); //.pop(e)
                return temp;
            } catch (Exception e) {
                return null;
            }
        } else if (queueMem) {
            try {
                temp = memory.removeFirst();
                return temp;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static double genRandcoord() { //generates a random coordinate in int
        double e = (Math.random() * 401) - 200;
        return e;
    }

    private static void Gwalk(Point cGoal, Robot Henry) { //regardless of what goal Henry wants to go to, this he will decide which way would be the most optimal
        Point goal = cGoal; //passes reference no matter if it is hungry or curious
        boolean xGoal = false, yGoal = false;

        //goal location
        double gPointx = goal.getX();
        double gPointy = goal.getY();

        //current location
        double cPointx = Henry.currentX();
        double cPointy = Henry.currentY();

        //if any of these come back true this will inform the code below if a snap will happen, if true then this robot will 'snap' towards whichever location
        boolean Snap = checkSnap(gPointx, gPointy, Henry);

        // if by any chance the value of the xValue or yValue is equal to the goal the robot will attempt to conserve energy and travel no,we,ea,so.
        if (cPointx == gPointx) {
            xGoal = true;
        }
        if (cPointy == gPointx) {
            yGoal = true;

        }

        // these points will also remind Henry that once a point is achieved, to start heading and getting the point
        if (cPointx < gPointx && cPointy < gPointy && !xGoal && !yGoal) {//North-east
            Henry.stepNorthEast();
            return;
        }
        if (cPointx > gPointx && cPointy < gPointy && !xGoal && !yGoal) { //NorthWest
            Henry.stepNorthWest();
            return;
        }
        if (cPointx < gPointx && cPointy > gPointy && !xGoal && !yGoal) {// southEast
            Henry.stepSouthEast();
            return;
        }
        if (cPointx > gPointx && cPointy > gPointy && !xGoal && !yGoal) {//Southwest
            Henry.stepSouthWest();
            return;
        }
        // when diagonals are not optimal
        if (cPointx < gPointx) { //east
            Henry.moveEast();
            return;
        }
        if (cPointx > gPointx) { // west
            Henry.moveWest();
            return;
        }
        if (cPointy < gPointy) { // north
            Henry.moveNorth();
            return;
        }
        if (cPointy > gPointy) { //west
            Henry.moveSouth();
            return;
        }
    }

    private static boolean checkSnap(double X2, double Y2, Robot Henry) { //method that detects snaps
        double X1 = Henry.currentX();
        double Y1 = Henry.currentY();
        int distance = (int) distancePts(X1, Y1, X2, Y2); // maybe change back to double
        if (distance <= snapDistance) {
            Henry.movement(distance);//costs movement
            Henry.setLocation(X2, Y2);
            return true;
        }
        return false;
    }

    private static double distancePts(double X1, double Y1, double X2, double Y2) {
        double distance = Math.sqrt(Math.pow((X2 - X1), 2) + Math.pow((Y2 - Y1), 2));
        return distance;
    }

    private static void detectEnergy(Robot Henry, boolean stackMem, boolean queueMem, Deque<Energy> memory) {
        Energy temp = Henry.detect();

        if (temp != null) {
            boolean reached = temp.isReached();
            if (reached) {
                return;
            }
            if (stackMem && !memory.contains(temp)) { //stackMemory
                memory.addFirst(temp); //.push(e)
                temp.setReached(true);
                return;
            } else if (queueMem && !memory.contains(temp)) { //queueMemoryStyle
                memory.addLast(temp);
                temp.setReached(true);
                return;
            }
        }
    }

    private static boolean checkStack(int simulationRun) {
        if (simulationRun == 0) {
            return true;
        }
        return false;
    }

    private static boolean checkQueue(int simulationRun) {
        if (simulationRun == 1) {
            return true;
        }
        return false;
    }
}