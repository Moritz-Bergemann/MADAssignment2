package com.moritzbergemann.myapplication.model;

import android.content.Context;
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
    private Settings settings;
    private int money;
    private int latestIncome;
    private int gameTime;
    private boolean gameStarted;
    List<UIUpdateObserver> uiUpdateObservers;
    private MutableLiveData<Boolean> gameLost;
    private GameMap map;

    private SQLiteDatabase db;

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
                null, null, null, null, null,
                null, null));

        try {
            if (gameDataCursor.moveToFirst()) { //If entry in cursor exists
                gameTime = gameDataCursor.getInt(
                        gameDataCursor.getColumnIndex(DatabaseSchema.GamesTable.Cols.TIME));
                money = gameDataCursor.getInt(gameDataCursor.getColumnIndex(
                        DatabaseSchema.GamesTable.Cols.MONEY));
                int gameStartedInt = gameDataCursor.getInt(gameDataCursor.getColumnIndex(DatabaseSchema.GamesTable.Cols.GAME_STARTED));
                if (gameStartedInt == 0) {
                    gameStarted = false;
                } else {
                    gameStarted = true;
                }
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

    public void spendMoney(int cost) throws MoneyException {
        int newMoney = money - cost;
        if (newMoney < 0) {
            throw new MoneyException("Insufficient money!");
        } else {
            money = newMoney;
        }
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
    }
}
