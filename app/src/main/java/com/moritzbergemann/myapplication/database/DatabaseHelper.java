package com.moritzbergemann.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
                GamesTable.Cols.CITY_NAME + " TEXT, " +
                GamesTable.Cols.MONEY + " INTEGER, " +
                GamesTable.Cols.TIME + " INTEGER)");

        //Structures table
        db.execSQL("CREATE TABLE " + SettingsTable.NAME + "(" +
                StructuresTable.Cols.ID + " INTEGER, " +
                StructuresTable.Cols.TYPE + " TEXT, " +
                StructuresTable.Cols.ROW + " INTEGER, " +
                StructuresTable.Cols.COLUMN + " INTEGER)");

        //Settings table
        db.execSQL("CREATE TABLE " + StructuresTable.NAME + "(" +
                SettingsTable.Cols.ID + " INTEGER, " +
                SettingsTable.Cols.MAP_HEIGHT + " INTEGER, " +
                SettingsTable.Cols.MAP_WIDTH+ " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // No >:(
    }
}