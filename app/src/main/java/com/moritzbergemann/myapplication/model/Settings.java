package com.moritzbergemann.myapplication.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.moritzbergemann.myapplication.database.DatabaseSchema;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing game settings. Setters and getters exist for settings that may be changed.
 */
public class Settings {
    private static final String TAG = "Settings";

    //GLOBAL CONSTANTS
    public static final int MIN_MAP_WIDTH = 1;
    public static final int MAX_MAP_WIDTH = 500;
    public static final int MIN_MAP_HEIGHT = 1;
    public static final int MAX_MAP_HEIGHT = 25;
    public static final int MIN_INITIAL_MONEY = 0;
    public static final int MAX_INITIAL_MONEY = 99999999;
    public static final int MIN_FAMILY_SIZE = 1;
    public static final int MAX_FAMILY_SIZE = 20;
    public static final int MIN_SHOP_SIZE = 2;
    public static final int MAX_SHOP_SIZE = 100;
    public static final int MIN_SALARY = 0;
    public static final int MAX_SALARY = 100;
    public static final double MIN_TAX_RATE = 0.0;
    public static final double MAX_TAX_RATE = 1.0;
    public static final int MIN_SERVICE_COST = 1;
    public static final int MAX_SERVICE_COST = 20;
    public static final int MIN_STRUCTURE_COST = 0;
    public static final int MAX_STRUCTURE_COST = 99999;

    //Map Settings
    private int mapWidth = -1;
    private int mapHeight = -1;

    //Game Settings
    private String cityName = "Perth";
    private int initialMoney = 1000;
    private int familySize = 4;
    private int shopSize = 6;
    private int salary = 10;
    private double taxRate = 0.4;
    private int serviceCost = 2;

    private SQLiteDatabase db;
    private boolean checkedDatabaseEntryExists = false;
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

    /**
     * Attempts to load an existing Settings object from the Settings table in the database.
     * @param db Database to load information from
     * @return The loaded settings object
     */
    public static Settings loadFromDatabase(SQLiteDatabase db) {
        Settings settings = new Settings(db);

        CursorWrapper settingsCursor = new CursorWrapper(db.query(DatabaseSchema.SettingsTable.NAME,
                null, DatabaseSchema.SettingsTable.Cols.ID + " = ?",
                new String[]{String.valueOf(DEFAULT_DATABASE_SETTING_ID)}, null,
                null, null, null));

        try {
            if (settingsCursor.moveToFirst()) {
                //Confirm database entry exists
                settings.checkedDatabaseEntryExists = true;

                //Setting individual settings
                settings.mapWidth = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.MAP_WIDTH));
                settings.mapHeight = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.MAP_HEIGHT));
                settings.initialMoney = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.INITIAL_MONEY));
                settings.cityName = settingsCursor.getString(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.CITY_NAME));
                settings.familySize = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.FAMILY_SIZE));
                settings.shopSize = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.SHOP_SIZE));
                settings.salary = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.SALARY));
                settings.taxRate = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.TAX_RATE));
                settings.serviceCost = settingsCursor.getInt(settingsCursor.getColumnIndex(
                        DatabaseSchema.SettingsTable.Cols.SERVICE_COST));

                //Setting structure costs
                settings.structureCosts = new HashMap<>();
                settings.structureCosts.put(Structure.Type.ROAD, settingsCursor.getInt(
                        settingsCursor.getColumnIndex(DatabaseSchema.SettingsTable.Cols.ROAD_COST)));
                settings.structureCosts.put(Structure.Type.RESIDENTIAL, settingsCursor.getInt(
                        settingsCursor.getColumnIndex(DatabaseSchema.SettingsTable.Cols.RESIDENTIAL_COST)));
                settings.structureCosts.put(Structure.Type.COMMERCIAL, settingsCursor.getInt(
                        settingsCursor.getColumnIndex(DatabaseSchema.SettingsTable.Cols.COMMERCIAL_COST)));
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
        updateDatabaseEntry();
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;

        updateDatabaseEntry();
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;

        updateDatabaseEntry();
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;

        updateDatabaseEntry();
    }

    public void setFamilySize(int familySize) {
        this.familySize = familySize;

        updateDatabaseEntry();
    }

    public void setShopSize(int shopSize) {
        this.shopSize = shopSize;

        updateDatabaseEntry();
    }

    public void setSalary(int salary) {
        this.salary = salary;

        updateDatabaseEntry();
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;

        updateDatabaseEntry();
    }

    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;

        updateDatabaseEntry();
    }

    public void setStructureCost(Structure.Type type, int cost) {
        structureCosts.put(type, cost);

        updateDatabaseEntry();
    }

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

    private void updateDatabaseEntry() {
        if (!checkedDatabaseEntryExists) {
            //Check if entry exists (in case it exists form previous session)
            Cursor checkerCursor = db.query(DatabaseSchema.SettingsTable.NAME, null,
                    DatabaseSchema.SettingsTable.Cols.ID + " = ?",
                    new String[]{String.valueOf(DEFAULT_DATABASE_SETTING_ID)},
                    null, null, null);

            try {
                if (checkerCursor.getCount() == 0) { //If no database entry exists
                    //Add database entry

                    db.insert(DatabaseSchema.SettingsTable.NAME, null, getContentValues());
                } //No else - if one is already here, no need to do anything
            } finally {
                checkerCursor.close();
            }

            checkedDatabaseEntryExists = true;
        }

        //Update the database entry (now that it definitely exists)
        int rowsAffected = db.update(DatabaseSchema.SettingsTable.NAME, getContentValues(),
                DatabaseSchema.SettingsTable.Cols.ID + " = ?",
                new String[]{String.valueOf(DEFAULT_DATABASE_SETTING_ID)});
        Log.v(TAG, String.format("Updated settings database - %d rows affected", rowsAffected));
    }

    /**
     * @return A ContentValues object (for inserting into database) containing all editable
     *  information on this settings object
     */
    private ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.SettingsTable.Cols.ID, DEFAULT_DATABASE_SETTING_ID);
        cv.put(DatabaseSchema.SettingsTable.Cols.MAP_WIDTH, mapWidth);
        cv.put(DatabaseSchema.SettingsTable.Cols.MAP_HEIGHT, mapHeight);
        cv.put(DatabaseSchema.SettingsTable.Cols.INITIAL_MONEY, initialMoney);
        cv.put(DatabaseSchema.SettingsTable.Cols.CITY_NAME, cityName);
        cv.put(DatabaseSchema.SettingsTable.Cols.FAMILY_SIZE, familySize);
        cv.put(DatabaseSchema.SettingsTable.Cols.SHOP_SIZE, shopSize);
        cv.put(DatabaseSchema.SettingsTable.Cols.SALARY, salary);
        cv.put(DatabaseSchema.SettingsTable.Cols.TAX_RATE, taxRate);
        cv.put(DatabaseSchema.SettingsTable.Cols.SERVICE_COST, serviceCost);
        cv.put(DatabaseSchema.SettingsTable.Cols.ROAD_COST, structureCosts.get(Structure.Type.ROAD));
        cv.put(DatabaseSchema.SettingsTable.Cols.RESIDENTIAL_COST, structureCosts.get(Structure.Type.RESIDENTIAL));
        cv.put(DatabaseSchema.SettingsTable.Cols.COMMERCIAL_COST, structureCosts.get(Structure.Type.COMMERCIAL));

        return cv;
    }
}
