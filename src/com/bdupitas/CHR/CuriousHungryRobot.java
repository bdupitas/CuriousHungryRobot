package com.bdupitas.CHR;

interface CuriousHungryRobot { //holds all constants for now
    double ENERGY_INITIAL_CAPACITY = 300; //UPDATE #2 ROBOT starts at 300;
    int NUMBER_OF_ENERGY_HUBS = 15; //Also # of objects //UPDATE#2
    double PLANE_ENERGY_RADIUS = 200; //this is as far as the points are able to go,
    double REGULAR_MOVEMENT = 13; //cost of energy moving no, we,east,south
    double DIAGONAL_MOVEMENT = Math.sqrt(338);// cost of energy moving diagonal
    double ROBOT_ENERGY_CAPACITY = 400; //UPDATE #2
    double SNAP_DISTANCE = 9;
    double ROBOT_ROBOT_DETECTION_RADIUS = 13;
    double ENERGY_INTERVALS = 20; // holds the minimum interval between the energy hub
    int TRIAL_LIMIT = 1000;
}