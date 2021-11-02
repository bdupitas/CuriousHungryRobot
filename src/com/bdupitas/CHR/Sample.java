package com.bdupitas.CHR;

import java.util.*;

public class Sample {
    //Private/protected variable to hold Statistics
    private int numOfSim;
    private double median;
    private double mean;
    private double standardDeviation;
    private double min;
    private double max;
    private String name;
    protected ArrayList<Double> data;

    Sample() {//default constructor
        this.data = new ArrayList<>();
    }

    void setName(String title) {
        this.name = title;
    }

    public String getName() {
        return this.name;
    }



    public double getSTdev() {
        return this.standardDeviation;
    }

    void setSTdev(double stDev) {
        this.standardDeviation = stDev;
    }

    public int getTrials() {
        return this.numOfSim;
    }

    double listAt(int i) {
        return this.data.get(i);

    }

    public String toString() {
        return String.format("%s: size = %d, min = %.4f, max = %.4f,\n mean = %.4f , median  = %.4f, sd = %.4f\n\r", this.getName(), this.getTrials(), this.getMin(), this.getMax(), this.getMean(), this.getMedian(), this.getSTdev());
    }

    void sortData(ArrayList<Double> data) {//sorts data from low to high
        Collections.sort(data);
    }

    void setMin(double d) {
        this.min = d;
    }

    public double getMin() {
        return this.min;
    }

    private void setMax(double d) {
        this.max = d;
    }

    public double getMax() {
        return this.max;
    }

    private void setMean(double d) {
        this.mean = d;
    }

    public double getMean() {
        return this.mean;
    }

    private void setMedian(double d) {
        this.median = d;
    }

    public double getMedian() {
        return this.median;
    }

    public void computeStats() {
        sortData(this.data);//also this method sorts the data

        this.setMin(Collections.min(data));
        this.setMax(Collections.max(data));

        this.setMean(calcMean(data));
        this.setMedian(calcMedian(data));
        this.setSTdev(calcSD(data));
    }

    private double calcMean(ArrayList<Double> data) {// returns the mean
        double result = 0;
        for(double val: data){
            result += val;
        }

        return result / data.size();
    }

    private double calcMedian(ArrayList<Double> data) {
        int size = this.data.size();//size
        double runTot;
        double finTotal ;

        if (size % 2 == 0) {
            runTot = listAt(size / 2) + data.get((size + 1) / 2);
            finTotal = runTot / 2;
        } else {
            finTotal = data.get(size / 2);
        }
        return finTotal;
    }

    private double calcSD(ArrayList<Double> data) {
        final double mean = getMean();
        double runTot = 0;
        double size = getTrials();
        for (int k = 0; k < getTrials(); k++) {
            runTot += Math.pow(listAt(k) - mean, 2);
        }
        return Math.pow(runTot / size, 0.5);
    }

    public void addData(double d) {
        data.add(d);
        this.numOfSim += 1;
    }

    double getRandomVal() {
        Random r = new Random();
        return data.get(r.nextInt(data.size()));
    }
}
