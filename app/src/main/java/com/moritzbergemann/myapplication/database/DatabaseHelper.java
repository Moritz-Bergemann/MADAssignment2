package com.moritzbergemann.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.moritzbergemann.myapplication.database.DatabaseSchema.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Games table
        db.execSQL("CREATE TABLE " + GamesTable.NAME + "(" +
                GamesTable.Cols.ID + " INTEGER PRIMARY KEY, " +
                GamesTable.Cols.MONEY + " INTEGER, " +
                GamesTable.Cols.TIME + " INTEGER, " +
                GamesTable.Cols.GAME_STARTED + " INTEGER)");

        //Map element table
        db.execSQL("CREATE TABLE " + MapElementTable.NAME + "(" +
                MapElementTable.Cols.GAME_ID + " INTEGER, " +
                MapElementTable.Cols.STRUCTURE_TYPE_ID + " TEXT, " +
                MapElementTable.Cols.IMAGE_BITMAP + " BLOB, " + //FIXME this probs not right
                MapElementTable.Cols.ROW + " INTEGER, " +
                MapElementTable.Cols.COLUMN + " INTEGER, " +
                MapElementTable.Cols.OWNER_NAME + " TEXT, " +
                "PRIMARY KEY (" + MapElementTable.Cols.GAME_ID + ", " + MapElementTable.Cols.ROW + ", " + MapElementTable.Cols.COLUMN + "))"); //Concatenated primary key of game ID, row & column position

        //Settings table
        db.execSQL("CREATE TABLE " + SettingsTable.NAME + "(" +
                SettingsTable.Cols.ID + " INTEGER, " +
                SettingsTable.Cols.MAP_WIDTH + " INTEGER, " +
                SettingsTable.Cols.MAP_HEIGHT + " INTEGER, " +
                SettingsTable.Cols.INITIAL_MONEY + " INTEGER, " +
                SettingsTable.Cols.CITY_NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // No :)
    }
}