package com.moritzbergemann.myapplication.database;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseUtils {
    private static DatabaseUtils instance = null;

    public static DatabaseUtils get() {
        if (instance == null) {
            instance = new DatabaseUtils();
        }

        return instance;
    }

    private SQLiteDatabase db;

    private DatabaseUtils() {
        this.db = null;
    }

    public void initialise() {

    }
}
