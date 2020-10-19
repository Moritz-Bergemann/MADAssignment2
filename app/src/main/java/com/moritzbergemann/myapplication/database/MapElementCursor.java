package com.moritzbergemann.myapplication.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.moritzbergemann.myapplication.model.GameMap;
import com.moritzbergemann.myapplication.model.MapElement;
import com.moritzbergemann.myapplication.database.DatabaseSchema.MapElementTable;

public class MapElementCursor extends CursorWrapper {
    public MapElementCursor(Cursor cursor) {
        super(cursor);
    }

    public void addMapElement(GameMap map) {
        int rowPos = getInt(getColumnIndex(MapElementTable.Cols.ROW));
        int colPos = getInt(getColumnIndex(MapElementTable.Cols.COLUMN));


    }
}
