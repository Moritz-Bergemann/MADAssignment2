package com.moritzbergemann.myapplication.model;

public class GameData {
    private static GameData instance = null;

    public static GameData get() {
        if (instance == null) {
            instance = new GameData();
        }

        return instance;
    }

    // FIELDS
    private Settings settings;
    private int money;
    private int gameTime;

    private GameMap map;
    private Structure selectedStructure;


    private GameData() {
        settings = new Settings();
        money = settings.getInitialMoney();
        gameTime = 0;
        map = new GameMap(settings.getMapHeight(), settings.getMapWidth());
        selectedStructure = null;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void timeStep() {
        gameTime++;
        money += getMoneyPerTurn();
    }

    public Settings getSettings() {
        return settings;
    }

    public GameMap getMap() {
        return map;
    }

    public int getMoney() {
        return money;
    }

    public Structure getSelectedStructure() {
        return selectedStructure;
    }

    public void setSelectedStructure(Structure selectedStructure) {
        this.selectedStructure = selectedStructure;
    }

    // Game statistics stuff

    public int getPopulation() {
        return settings.getFamilySize() * map.getStructureAmount(Structure.Type.RESIDENTIAL);
    }

    public double getEmploymentRate() {
        return Math.min(1.0, map.getStructureAmount(Structure.Type.COMMERCIAL) *
                settings.getShopSize() / (float)getPopulation());
    }

    public int getMoneyPerTurn() {
        return getPopulation() *
                ((int)(getEmploymentRate() * settings.getSalary() * settings.getTaxRate())
                - settings.getServiceCost());
    }
}
