
package com.bdupitas.calcApp;//Mind and States of the Robot
import com.bdupitas.calcApp.Sample;
import com.bdupitas.calcApp.energy;
import com.bdupitas.calcApp.robot;

import java.awt.Point;
import java.util.*;

public class Main extends robot {
    public static void main(String args[]){
        int simulationRun = 0;// 0 - stack, 1 - queue
        while(simulationRun <2 ){
            Sample data = new Sample(); //allows for data to be held during this simulation
            int trialNo = 0; //up to 1000
            int mileage = 0; // the distance traveled
            while(trialNo<simulationTrial){
                robot Henry = new robot();
                Henry.radarCheck();
                Point cGoal = null; // cgoal is jut a point
                energy hGoal = null; //is a point of energy
                Point hGoalPt =null ; // under the same umbrella ^
                Deque<energy> memory = new ArrayDeque<energy>(); // Memory of Points

                final boolean stackMem= checkStack(simulationRun);// memory style
                final boolean queueMem = checkQueue(simulationRun);

                //all booleans set to false, as the robot will verify the energy before allowing
                boolean cState = false, hState = false, cGoalExists = false, hGoalExists = false,cGoalreached =false, hGoalReached = false;


                cState = checkCurious(Henry,cState);
                do{
                    if(cState){ //curious state

                        if(cGoalExists!= true){
                            cGoal = getCgoal();
                            cGoalExists = true;
                            cGoalreached = !cGoalExists;
                        }
                        else if(cGoalExists&& !cGoalreached) {
                            Gwalk(cGoal,Henry); //goal Walk
                            cGoalreached = gReached(cGoal, Henry);
                            cGoalExists = !cGoalreached;

                        }

                    }
                    if(hState){ //hungry state

                        if(hGoalExists !=true){
                            hGoal = getHgoal(memory,stackMem,queueMem,cState,Henry);
                            if(hGoal == null){ //switches cases for this move
                                cState = true;
                                hState = false;
                                continue;
                            }
                            if(hGoal != null){
                                hGoalExists = true;
                                hGoalReached = !hGoalExists;
                                int x = (int)(hGoal.getX());
                                int y = (int)(hGoal.getY());
                                hGoalPt = new Point(x,y);
                            }
                        }
                        else if(hGoalExists && !hGoalReached){
                            Gwalk(hGoalPt,Henry);
                            hGoalReached = gReached(hGoalPt, Henry);


                        }
                        if(hGoalReached){
                            Henry.consumeEnergy(hGoal);
                            hGoalExists = !hGoalReached;
                            hGoalReached = false; //once consumed a new goal should be set
                        }

                    }
                    cState = checkCurious(Henry,cState);
                    hState = checkHungry( Henry, hState);
                    detectEnergy(Henry,stackMem,queueMem,memory);
                }
                while((Henry.getEnergy())>0);
                inactive(Henry,data); //eventually have this report total movement to sample. sample to recurse
                trialNo++;

            }
            if(simulationRun ==0){
                data.setName("Stack Memory");
            }
            else if(simulationRun ==1){
                data.setName("Queue Memory");
            }


            if(simulationRun !=2){
                data.computeStats();
                System.out.printf(data.toString());

            }
            simulationRun++;
        }
    }

    static void inactive(robot Henry, Sample data){ // save statistics for this trial, incrememnt to 1000, then recurse main
        double x = Henry.getMileage();
        data.addData(x);
    }
    static boolean checkCurious(robot Henry, boolean cState){ //checks if the robot is curious
        if(Henry.getEnergy() >(robotEnergyCapacity/2)){
            cState = true;
        }
        else{
            cState = false;
        }
        return cState;
    }


    static boolean checkHungry(robot Henry, boolean hState){ // checks if the robot is hungry
        if((Henry.getEnergy()<=(robotEnergyCapacity/2))&& Henry.getEnergy()>0){
            hState = true;
        }
        else{
            hState = false;
        }
        return hState;
    }


    private static boolean gReached(Point cGoal, robot Henry){ //determines if goal has been reached
        Point goal  = cGoal;
        boolean xGoal = false, yGoal=false;
        double gPointx = goal.getX();
        double gPointy = goal.getY();
        //current location
        double cPointx = Henry.currentX();
        double cPointy = Henry.currentY();
        if(cPointx == gPointx){
            xGoal =true;
        }
        if(cPointy ==gPointy){
            yGoal = true;
        }
        if(xGoal &&yGoal){
            return true;
        }
        else
            return false;
    }

    private static Point getCgoal(){ //curious goal.
        return new Point((int)genRandcoord(),(int)genRandcoord());
    }
    private static energy getHgoal(Deque<energy> memory, boolean stackMem, boolean queueMem, boolean cState, robot Henry ){ //returns a point of the memoryGoal
        energy temp = null;
        if(stackMem){
            try {
                temp = memory.removeFirst(); //.pop(e)
                return temp;
            }catch(Exception e)
            {
                return null;
            }
        }
        else if(queueMem){
            try {
                temp = memory.removeFirst();
                return temp;
            }catch(Exception e)
            {
                return null;
            }
        }
        return null;
    }

    private static double genRandcoord(){ //generates a random coordinate in int
        double e = (Math.random()*401)-200;
        return e;
    }

    private static void Gwalk(Point cGoal, robot Henry){ //reguardless of what goal Henry wants to go to, this he will decide which way would be the most optimal
        Point goal = cGoal; //passes reference no matter if it is hungy or curious
        boolean xGoal = false, yGoal=false;

        //goal location
        double gPointx = goal.getX();
        double gPointy = goal.getY();

        //current location
        double cPointx = Henry.currentX();
        double cPointy = Henry.currentY();

        //if any of these come back true this will inform the code below if a snap will happen, if true then this robot will 'snap' towards whichever location
        boolean Snap =checkSnap(gPointx, gPointy, Henry);

        // if by any chance the value of the xValue or yValue is equal to the goal the robot will attempt to conserve energy and travel no,we,ea,so.
        if(cPointx ==gPointx){
            xGoal = true;
        }
        if(cPointy==gPointx){
            yGoal =true;

        }

        // these points will also remind Henry that once a point is achieved, to start heading and getting the point
        if(cPointx < gPointx && cPointy < gPointy && !xGoal && !yGoal){//North east
            Henry.moveNE();
            return;
        }
        if(cPointx >gPointx && cPointy < gPointy&& !xGoal && !yGoal){ //NorthWest
            Henry.moveNW();
            return;
        }
        if(cPointx < gPointx && cPointy > gPointy &&!xGoal && !yGoal ){// southEast
            Henry.moveSE();
            return;
        }
        if (cPointx > gPointx && cPointy > gPointy &&!xGoal && !yGoal){//Southwest
            Henry.moveSW();
            return;
        }
        // when diagonals are not optimal
        if(cPointx < gPointx){ //east
            Henry.moveEast();
            return;
        }
        if(cPointx >gPointx){ // west
            Henry.moveWest();
            return;
        }
        if (cPointy < gPointy){ // north
            Henry.moveNorth();
            return;
        }
        if(cPointy> gPointy){ //west
            Henry.moveSouth();
            return;
        }
    }

    private static boolean checkSnap(double X2, double Y2, robot Henry){ //method that detects snaps
        double X1 = Henry.currentX();
        double Y1 = Henry.currentY();
        int distance = (int)distancePts(X1,Y1, X2, Y2); // maybe change back to double
        if(distance<=snapDistance){
            Henry.movement(distance);//costs movement
            Henry.setLocation(X2,Y2);
            return true;
        }
        return false;
    }

    private static double distancePts(double X1, double Y1, double X2, double Y2){
        double distance = Math.sqrt(Math.pow((X2-X1),2)+Math.pow((Y2-Y1),2));
        return distance;
    }

    private static void detectEnergy(robot Henry, boolean stackMem, boolean queueMem, Deque<energy> memory){
        energy temp = Henry.detect();

        if(temp != null ){
            boolean reached = temp.isReached();
            if(reached){
                return;
            }
            if(stackMem &&!memory.contains(temp)){ //stackMemory
                memory.addFirst(temp); //.push(e)
                temp.setReached(true);
                return;
            }
            else if(queueMem &&!memory.contains(temp)){ //queueMemoryStyle
                memory.addLast(temp);
                temp.setReached(true);
                return;
            }
        }
    }

    private static boolean checkStack( int simulationRun){
        if(simulationRun == 0){
            return true;
        }
        return false;
    }
    private static boolean checkQueue( int simulationRun){
        if(simulationRun == 1){
            return true;
        }
        return false;
    }
}