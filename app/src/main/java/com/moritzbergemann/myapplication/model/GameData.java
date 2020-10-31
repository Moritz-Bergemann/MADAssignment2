package com.moritzbergemann.myapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.moritzbergemann.myapplication.database.DatabaseHelper;
import com.moritzbergemann.myapplication.database.DatabaseSchema;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * MAIN TRACKING LOCATION
 * POTENTIAL TODOS:
 * TODO fix up string literals
 * TODO go over assignment spec and make sure I have everything
 * TODO change the logo
 */

/**
 * Singleton class for controlling primary game state.
 * Contains game settings and map as objects.
 * Also contains various utility functions for interacting with the game state
 */
public class GameData {
    private static final String TAG = "GameData";

    private static GameData instance = null;

    public static GameData get() {
        if (instance == null) {
            instance = new GameData();
        }

        return instance;
    }

    // FIELDS
    private Settings settings; //Game settings
    private int money; //Player's current money
    private int latestIncome; //Latest income (0 by default)
    private int gameTime; //Game time in time steps
    private boolean gameStarted; //Whether or not the game has started (used to control what settings
                                    // can be set
    List<UIUpdateObserver> uiUpdateObservers; //Observers for changes to game data
    private MutableLiveData<Boolean> gameLost;
    private GameMap map; //Game map

    private SQLiteDatabase db;
    private boolean checkedDatabaseEntryExists = false; //If it is known an entry exists in the
                                                        // database for the game
    public static final int DEFAULT_DATABASE_GAME_DATA_ID = 0;

    private GameData() {
        settings = new Settings(db);
        money = -1;
        latestIncome = 0;
        gameTime = 0;
        map = null;
        uiUpdateObservers = new LinkedList<>();
        gameStarted = false;
        gameLost = new MutableLiveData<>(false);

        db = null;
    }

    /**
     * Attempts to load game from database - overwrites settings and map if game to load found
     */
    public void loadGame(Context context) {
        if (instance == null) {
            instance = new GameData();
        }

        db = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();

        //Load game information into GameData if it exists
        CursorWrapper gameDataCursor = new CursorWrapper(db.query(DatabaseSchema.GamesTable.NAME,
                null, DatabaseSchema.GamesTable.Cols.ID + " = ?",
                new String[]{String.valueOf(DEFAULT_DATABASE_GAME_DATA_ID)},
                null, null, null, null));

        try {
            if (gameDataCursor.moveToFirst()) { //If entry in cursor exists
                gameTime = gameDataCursor.getInt(
                        gameDataCursor.getColumnIndex(DatabaseSchema.GamesTable.Cols.TIME));
                money = gameDataCursor.getInt(gameDataCursor.getColumnIndex(
                        DatabaseSchema.GamesTable.Cols.MONEY));
                int gameStartedInt = gameDataCursor.getInt(gameDataCursor.getColumnIndex(DatabaseSchema.GamesTable.Cols.GAME_STARTED));
                gameStarted = gameStartedInt != 0;
            }
        } finally {
            gameDataCursor.close();
        }

        //Load in settings from database (map is loaded at construction)
        settings = Settings.loadFromDatabase(db);

        //Load in map if the game has started (otherwise map should still be null)
        if (gameStarted) {
            map = GameMap.loadFromDatabase(db, settings.getMapWidth(), settings.getMapHeight());
        }
    }

    /**
     * Reset the entire app to its initial state. Includes wiping the database.
     */
    public static void resetAll(Context context) {
        //Reset GameData itself to a new instance
        instance = new GameData();

        //Wipe the database
        new DatabaseHelper(context).deleteDatabase();

        //Reload/initialise everything
        GameData.get().loadGame(context);
    }

    public void spendMoney(int cost) throws MoneyException {
        int newMoney = money - cost;
        if (newMoney < 0) {
            throw new MoneyException("Insufficient money!");
        } else {
            money = newMoney;
        }

        updateDatabaseEntry();
    }

    public void gainMoney(int money) {
        this.money += money;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void timeStep() {
        latestIncome = getMoneyPerTurn();

        gameTime++;
        money += getMoneyPerTurn();

        //Loss condition check
        if (money < 0) {
            gameLost.postValue(true);
        }

        notifyUIUpdate();

        updateDatabaseEntry();
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

    //**Game statistics stuff**
    public int getPopulation() {
        return settings.getFamilySize() * map.getStructureAmount(Structure.Type.RESIDENTIAL);
    }

    public double getEmploymentRate() {
        return Math.min(1.0, (float) map.getStructureAmount(Structure.Type.COMMERCIAL) *
                settings.getShopSize() / (float) getPopulation());
    }

    public int getMoneyPerTurn() {
        Log.v(TAG, String.format(Locale.US, "Getting money per turn: Employment " +
                "Rate %f, Salary %d, Tax Rate %f, Service Cost %d", getEmploymentRate(),
                settings.getSalary(), settings.getTaxRate(), settings.getServiceCost()));

        return getPopulation() *
                ((int) (getEmploymentRate() * settings.getSalary() * settings.getTaxRate())
                        - settings.getServiceCost());
    }

    public LiveData<Boolean> getGameLost() {
        return gameLost;
    }

    public void addUIUpdateObserver(UIUpdateObserver observer) {
        uiUpdateObservers.add(observer);
    }

    public void notifyUIUpdate() {
        for (UIUpdateObserver observer : uiUpdateObservers) {
            observer.updateUI();
        }
    }

    public int getLatestIncome() {
        return latestIncome;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void startGame() {
        gameStarted = true;
        map = new GameMap(db, settings.getMapHeight(), settings.getMapWidth());
        money = settings.getInitialMoney();

        updateDatabaseEntry();
    }

    /**
     * Updates the GameData entry in the database
     */
    private void updateDatabaseEntry() {
        if (!checkedDatabaseEntryExists) { //If we haven't already checked that the entry exists
            //Check if entry exists (in case it exists form previous session)
            Cursor checkerCursor = db.query(DatabaseSchema.GamesTable.NAME, null,
                    DatabaseSchema.GamesTable.Cols.ID + " = ?",
                    new String[]{String.valueOf(DEFAULT_DATABASE_GAME_DATA_ID)},
                    null, null, null);

            try {
                if (checkerCursor.getCount() == 0) { //If no database entry exists
                    //Add database entry

                    db.insert(DatabaseSchema.GamesTable.NAME, null, getContentValues());
                } //No else - if one is already here, no need to do anything
            } finally {
                checkerCursor.close();
            }
        }

        //Update the database entry (now that it definitely exists)
        db.update(DatabaseSchema.GamesTable.NAME, getContentValues(),
                DatabaseSchema.GamesTable.Cols.ID + " = ?",
                new String[]{String.valueOf(DEFAULT_DATABASE_GAME_DATA_ID)});
    }

    private ContentValues getContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(DatabaseSchema.GamesTable.Cols.ID, DEFAULT_DATABASE_GAME_DATA_ID);
        cv.put(DatabaseSchema.GamesTable.Cols.MONEY, money);
        cv.put(DatabaseSchema.GamesTable.Cols.TIME, gameTime);

        int gameStartedInt;
        if (gameStarted) {
            gameStartedInt = 1;
        } else {
            gameStartedInt = 0;
        }
        cv.put(DatabaseSchema.GamesTable.Cols.GAME_STARTED, gameStartedInt);


        return cv;
    }
}
