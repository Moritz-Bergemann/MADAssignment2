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
                GamesTable.Cols.ID + " INTEGER, " +
                GamesTable.Cols.MONEY + " INTEGER, " +
                GamesTable.Cols.TIME + " INTEGER, " +
                GamesTable.Cols.GAME_STARTED + " INTEGER)");

        //Structures table
        db.execSQL("CREATE TABLE " + MapElementTable.NAME + "(" +
                MapElementTable.Cols.ID + " INTEGER, " +
//                MapElementTable.Cols.TYPE + " TEXT, " +
                MapElementTable.Cols.STRUCTURE_INDEX + " INTEGER, " +
                MapElementTable.Cols.IMAGE_BITMAP + " IMAGE, " + //FIXME this probs not right
                MapElementTable.Cols.ROW + " INTEGER, " +
                MapElementTable.Cols.COLUMN + " INTEGER)");

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