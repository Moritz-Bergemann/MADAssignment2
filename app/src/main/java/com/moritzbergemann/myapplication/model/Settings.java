package com.moritzbergemann.myapplication.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.moritzbergemann.myapplication.database.DatabaseSchema;

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
    private int mapWidth = -1;
    private int mapHeight = -1;

    //Game Settings
    private String cityName = "Perth"; //TODO make setting for this
    private int initialMoney = -1;
    private int familySize = 4;
    private int shopSize = 6;
    private int salary = 10;
    private double taxRate = 0.4;
    private int serviceCost = 2;

    private SQLiteDatabase db;
    private boolean databaseEntryExists = false;
    public static final int DEFAULT_DATABASE_SETTING_ID = 0;

    //Building Costs
    private Map<Structure.Type, Integer> structureCosts;

    public Settings(SQLiteDatabase db) {
        structureCosts = new HashMap<>();
        structureCosts.put(Structure.Type.RESIDENTIAL, 100);
        structureCosts.put(Structure.Type.COMMERCIAL, 500);
        structureCosts.put(Structure.Type.ROAD, 20);

        this.db = db;
    }

    public static Settings loadFromDatabase(SQLiteDatabase db) {
        Settings settings = new Settings(db);

        CursorWrapper settingsCursor = new CursorWrapper(db.query(DatabaseSchema.SettingsTable.NAME,
                null, null, null, null, null,
                null, null));

        try {
            if (settingsCursor.moveToFirst()) {
                //Confirm database entry exists
                settings.databaseEntryExists = true;

                settings.mapWidth = settingsCursor.getInt(settingsCursor.getColumnIndex(DatabaseSchema.SettingsTable.Cols.MAP_WIDTH));
                settings.mapHeight = settingsCursor.getInt(settingsCursor.getColumnIndex(DatabaseSchema.SettingsTable.Cols.MAP_HEIGHT));
                settings.cityName = settingsCursor.getString(settingsCursor.getColumnIndex(DatabaseSchema.SettingsTable.Cols.CITY_NAME));
            }
        } finally {
            settingsCursor.close();
        }

        return settings;
    }

    //***GETTERS***
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

    //***SETTERS***
    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;

        //Save to DB
        updateSettingsEntry();
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;

        updateSettingsEntry();
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;

        updateSettingsEntry();
    }

    //TODO city name stuff

    /**
     * @return Whether all settings required to start the game have been set by the user.
     */
    public boolean areEssentialSettingsSet() {
        boolean allSet = false;

        if (mapHeight != -1) {
            if (mapWidth != -1) {
                if (initialMoney != -1) {
                    allSet = true;
                }
            }
        }

        return allSet;
    }

    private void updateSettingsEntry() {
        if (!databaseEntryExists) {
            //Check if entry exists (in case it exists form previous session)
            Cursor checkerCursor = db.query(DatabaseSchema.SettingsTable.NAME, null, //FIXME possibly unnecessary
                    DatabaseSchema.SettingsTable.Cols.ID + " = ?",
                    new String[]{String.valueOf(DEFAULT_DATABASE_SETTING_ID)},
                    null, null, null);
            if (checkerCursor.getCount() == 0) { //If no database entry exists
                //Add database entry

                db.insert(DatabaseSchema.SettingsTable.NAME, null, getContentValues());
            } //If one is already here, no need to do anything

            databaseEntryExists = true;
        }

        //Update database entry
        db.update(DatabaseSchema.SettingsTable.NAME, getContentValues(),
                DatabaseSchema.SettingsTable.Cols.ID + "= ?",
                new String[]{String.valueOf(DEFAULT_DATABASE_SETTING_ID)});
    }

    /**
     * @return A ContentValues object (for inserting into database) containing all editable
     *  information on this settings object
     */
    private ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.SettingsTable.Cols.MAP_WIDTH, mapWidth);
        cv.put(DatabaseSchema.SettingsTable.Cols.MAP_HEIGHT, mapHeight);
        cv.put(DatabaseSchema.SettingsTable.Cols.CITY_NAME, cityName);

        return cv;
    }
}
