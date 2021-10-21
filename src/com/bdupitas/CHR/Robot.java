package com.bdupitas.CHR;

import java.awt.Point;

//The physical state of the robot and physical battery
public class Robot implements CuriousHungryRobot {
   private Energy robotBattery; //holds the current energy level of Henry; however this will serve as a way to access energy
   private Point gps; 
   private Energy radar; // the energy is a force
   private double mileage;
       
   public Robot(){//initializer
      this.robotBattery = new Energy(energyInitialCapacity);
      this.gps = new Point();
      this.radar = new Energy();
      this.mileage=0;
   }
   public double getEnergy(){
      return robotBattery.checkBattery();
   
   }
   public double getMileage(){
      return this.mileage;
   }
   //movements
   protected void WAI(){ //returns location on x,y 
      System.out.println( this.gps.getLocation());
   }
   public double currentX(){
      return this.gps.getX();
   }
   public double currentY(){
      return this.gps.getY(); 
   }
   public void setLocation(double X2, double Y2){
      this.gps.setLocation(X2,Y2);
   
   }
   
   protected void moveNorth(){ //north   +Y
      movement(eRegMovm); // drainenergy
      this.gps.setLocation(this.gps.getX(),this.gps.getY()+eRegMovm);
   }
   
   protected void moveSouth(){ //move south  -Y
      movement(eRegMovm); // drainenergy
      this.gps.setLocation(this.gps.getX(),this.gps.getY()-eRegMovm);
   }
   
   protected void moveWest(){ //moves west -x
      movement(eRegMovm);
      this.gps.setLocation(this.gps.getX()-eRegMovm,this.gps.getY());
   }
   
   protected void moveEast(){ //moves east +x
      movement(eRegMovm);
      this.gps.setLocation(this.gps.getX()+eRegMovm,this.gps.getY());
   }
   
   protected void snapStraight(double x){
      movement(x);
   }

   public void setRobX(double x){ //sets the x value after a snap distance
      this.gps.setLocation(x, gps.getY());
   }
   public void setRobY(double y){
      this.gps.setLocation( gps.getX(),y);
   
   }

   protected void moveNE(){ //DIAGONAL Northeast  +x +y
      movement(diagMovm);
      this.gps.setLocation(this.gps.getX()+eRegMovm, this.gps.getY()+eRegMovm);
   }
   
   protected void moveNW(){ //DIAGONAL Northwest -x +y
      movement(diagMovm);
      this.gps.setLocation(this.gps.getX()-eRegMovm, this.gps.getY()+eRegMovm);
   }
   
   protected void moveSW(){ //DIAGONAL southWest -x -y
      movement(diagMovm);
      this.gps.setLocation(this.gps.getX()-eRegMovm,this.gps.getY()-eRegMovm);
   }
   
   protected void moveSE(){ //DIAGONAL SouthEast  +x -y
      movement(diagMovm);
      this.gps.setLocation(this.gps.getX()+eRegMovm,this.gps.getY()-eRegMovm);
   }

   protected void movement(double v){
      this.mileage+=v;
      this.robotBattery.DrainEnergy(v); //the state of power comes from energy, but is within the battery pack of the robot
   }   
   protected void radarCheck(){ //this tells energy to be placed on the grid
      this.radar.EnergyPoint();
   }
   protected Energy detect(){        //detection
      double rX = this.gps.getX();
      double rY= this.gps.getY();
      Energy temp = radar.ping(rX,rY);
      if(temp != null){
         return temp;
      }
      return null;
   } 
   protected void consumeEnergy(Energy hGoal){
      Energy temp = hGoal;
      double charge = hGoal.getCharge();
      double currentBatt = this.robotBattery.checkBattery();
      if(charge+currentBatt>robotEnergyCapacity){
         double leftover = charge+ currentBatt - robotEnergyCapacity;
         temp.setCharge(leftover);
         this.robotBattery.recharge(robotEnergyCapacity);
         temp.setReached(false); // flips it back to false if there is leftover energies
      }
      else{
         this.robotBattery.recharge(currentBatt+charge);
         temp.deplete();
      }
      
   
   }
   
}