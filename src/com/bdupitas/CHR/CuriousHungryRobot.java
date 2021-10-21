package com.bdupitas.CHR;

interface CuriousHungryRobot { //holds all constants for now
    final double energyInitialCapacity = 300; //UPDATE #2 ROBOT starts at 300;
    final int numberOfEnergyHubs = 33; //Also # of objects //UPDATE#2
    final double planeEnergyRadius = 200; //this is as far as the points are able to go,
    final double eRegMovm = 13; //cost of energy moving no, we,east,south
    final double diagMovm = Math.sqrt(338);// cost of energy moving diagonal
    final double robotEnergyCapacity = 400; //UPDATE #2
    final double snapDistance = 9;
    final double robotRobotDetectionRadius = 13;
    final double eID = 20; // holds the minimum interval between the energy hub
    final int simulationTrial = 1000;
}