package com.bdupitas.calcApp;

import java.util.*;
public class Sample{
//Private/protected variable to hold Statistics
   private int numOfSim;
   private double median;
   private double mean;
   private double STdev;
   private double min;
   private double max;
   private Random r;
   private String name;
   protected ArrayList<Double> data;
   
   Sample(){//default constructor 
      this.data = new ArrayList<>();
   }
   
   void setName(String title){
      this.name = title;
   }
   
   public String getName(){
      String name = this.name;
      return name;
   }
   
   public void clearData(){
      this.data.clear();
   }
   
 
   public double getSTdev(){
      double d = this.STdev;
      return d;
   }
   
   void setSTdev(double stDev){
      this.STdev = stDev; 
   }  
   
   public int getTrials(){
      int d = this.numOfSim;
      return d;
   }
   
   double listAt(int i){
      double d = this.data.get(i);
      return d;
   }

   public String toString(){
      return String.format("%s: size = %d, min = %.4f, max = %.4f,\n mean = %.4f , median  = %.4f, sd = %.4f\n\r",this.getName(),this.getTrials(),this.getMin(),this.getMax(), this.getMean(),this.getMedian(),this.getSTdev());
   }
   
   void sortData(ArrayList<Double> data){//sorts data from low to high
      Collections.sort(data);
   }
   
   void setMin(double d){
      this.min = d;
   
   }
   
   public double getMin(){
      double d = this.min;
      return d;
   }
   
   private void setMax(double d){
      this.max = d; 
   }
   
   public double getMax(){
      double d = this.max;
      return d;
   }
   
   private void setMean(double d){
      this.mean = d;
   }
   
   public double getMean(){
      double d = this.mean;
      return d;
   }
   
   private void setMedian(double d){
      this.median = d;
   }
   
   public double getMedian(){
      double d = this.median;
      return d;
   }
   
   public void computeStats() {
      sortData(this.data);//also this method sorts the data
   
      int i = this.getTrials();
   
      this.setMin(data.get(0));
      this.setMax(data.get(i-1));
      
      this.setMean(calcMean(data));
      this.setMedian(calcMedian(data));
      this.setSTdev(calcSD(data));
   }
 
   private double calcMean(ArrayList<Double> data){// returns the mean
      int size = this.data.size();//size
      //test
      double runTot= 0;
      for(int i= 0; i<size;i++){
         runTot = runTot + (listAt(i));
      }
      return runTot/size;
   }
   
   private double calcMedian(ArrayList<Double> data){
      int size = this.data.size();//size
      double runTot= 0;
      double finTotal = 0; 
      
      if(size%2 ==0){
         runTot = listAt(size/2) + data.get((size+1)/2);
         finTotal = runTot/2;
      }
      else {
         finTotal = data.get(size/2);
      }
      return finTotal; 
   }
   
   private double calcSD(ArrayList<Double> data){
      final double mean = getMean(); 
      double runTot = 0;
      double size = getTrials();
      for(int k = 0; k<getTrials(); k++){
         runTot+= Math.pow(listAt(k) - mean,2);
      }
      return Math.pow(runTot /size,0.5);
   }   
    
   public void addData(double d){
      data.add(d);
      this.numOfSim +=1;
   }
   
   double getRandomVal(){
      Random r = new Random();
      double value = data.get(r.nextInt(data.size()));
      return value; 
   }
}
