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

    private GameData() {
        settings = new Settings();
        money = settings.getInitialMoney();
        gameTime = 0;
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

    public int getMoney() {
        return money;
    }
}
