package com.bdupitas.CHR;

interface CuriousHungryRobot { //holds all constants for now
    final double ENERGY_INITIAL_CAPACITY = 300; //UPDATE #2 ROBOT starts at 300;
    final int NUMBER_OF_ENERGY_HUBS = 15; //Also # of objects //UPDATE#2
    final double PLANE_ENERGY_RADIUS = 200; //this is as far as the points are able to go,
    final double REGULAR_MOVEMENT = 13; //cost of energy moving no, we,east,south
    final double DIAGONAL_MOVEMENT = Math.sqrt(338);// cost of energy moving diagonal
    final double ROBOT_ENERGY_CAPACITY = 400; //UPDATE #2
    final double SNAP_DISTANCE = 9;
    final double ROBOT_ROBOT_DETECTION_RADIUS = 13;
    final double ENERGY_INTERVALS = 20; // holds the minimum interval between the energy hub
    final int TRIAL_LIMIT = 1000;
}