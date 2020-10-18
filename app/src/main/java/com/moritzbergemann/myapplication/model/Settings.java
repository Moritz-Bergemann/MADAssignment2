package com.moritzbergemann.myapplication.model;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    //GLOBAL CONSTANTS
    public static final int MIN_MAP_WIDTH = 1;
    public static final int MAX_MAP_WIDTH = 500;
    public static final int MIN_MAP_HEIGHT = 1;
    public static final int MAX_MAP_HEIGHT = 25;
    public static final int MIN_INITIAL_MONEY = 620;
    public static final int MAX_INITIAL_MONEY = 99999999;

    //Map Settings
    private int mapWidth = 50;
    private int mapHeight = 10;

    //Game Settings
    private String cityName = "Perth";
    private int initialMoney = 1000;
    private int familySize = 4;
    private int shopSize = 6;
    private int salary = 10;
    private double taxRate = 0.4;
    private int serviceCost = 2;

    //Building Costs
    private Map<Structure.Type, Integer> structureCosts;

    public Settings() {
        structureCosts = new HashMap<>();
        structureCosts.put(Structure.Type.RESIDENTIAL, 100);
        structureCosts.put(Structure.Type.COMMERCIAL, 500);
        structureCosts.put(Structure.Type.ROAD, 20);
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public int getStructureCost(Structure.Type type) {
        Integer structureCost = structureCosts.get(type);
        if (structureCost == null) {
            throw new IllegalArgumentException("Structure type not found");
        }

        return structureCost;
    }

    public int getFamilySize() {
        return familySize;
    }

    public int getShopSize() {
        return shopSize;
    }

    public int getSalary() {
        return salary;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public String getCityName() {
        return cityName;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;
    }
}
