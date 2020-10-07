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

    private GameData() {
        settings = new Settings();
        money = settings.getInitialMoney();
        gameTime = 0;
        map = new GameMap(settings.getMapHeight(), settings.getMapWidth());
    }

    public int getGameTime() {
        return gameTime;
    }

    public void timeStep() {
        gameTime++;
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
}
