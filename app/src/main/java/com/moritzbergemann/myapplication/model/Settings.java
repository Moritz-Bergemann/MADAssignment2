package com.moritzbergemann.myapplication.model;

public class Settings {
    //Map Settings
    private int mapWidth = 50;
    private int mapHeight = 10;

    //Game Settings
    private int initialMoney = 1000;
    private int familySize = 4;
    private int shopSize = 6;
    private int salary = 10;
    private double taxRate = 0.4;
    private int serviceCost = 2;

    //Building Costs
    private int houseBuildingCost = 100;
    private int commercialBuildingCost = 500;
    private int roadBuildingCost = 20;

    public Settings() { }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getInitialMoney() {
        return initialMoney;
    }
}
