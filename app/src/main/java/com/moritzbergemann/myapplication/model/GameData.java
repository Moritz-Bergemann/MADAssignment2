package com.moritzbergemann.myapplication.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Observer;

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
    private int gameTime;
    List<UIUpdateObserver> uiUpdateObservers;
    private MutableLiveData<Boolean> gameLost;
    private GameMap map;

    private GameData() {
        settings = new Settings();
        money = settings.getInitialMoney();
        gameTime = 0;
        map = new GameMap(settings.getMapHeight(), settings.getMapWidth());
        uiUpdateObservers = new LinkedList<>();
        gameLost = new MutableLiveData<>(false);
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
}
