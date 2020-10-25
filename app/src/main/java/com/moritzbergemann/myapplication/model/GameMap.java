package com.moritzbergemann.myapplication.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.moritzbergemann.myapplication.BuildConfig;
import com.moritzbergemann.myapplication.database.DatabaseSchema.MapElementTable;
import com.moritzbergemann.myapplication.database.MapElementCursor;

import java.util.HashMap;
import java.util.Map;

public class GameMap {
    // Fields
    private MapElement[][] map;
    private Map<Structure.Type, Integer> structureAmounts;

    private SQLiteDatabase db;
    private static final int DEFAULT_GAME_MAP_ID = 0;

    public GameMap(SQLiteDatabase db, int height, int width) {
        map = initialiseMap(height, width);
        structureAmounts = new HashMap<>();
        structureAmounts.put(Structure.Type.RESIDENTIAL, 0);
        structureAmounts.put(Structure.Type.COMMERCIAL, 0);
        structureAmounts.put(Structure.Type.ROAD, 0);

        this.db = db;
    }

    /**
     * Loads an existing map in from the database. Should only be called if game has already been
     *  started and map already exists
     * @param db database to load in from/save to in future
     * @param width map width
     * @param height map height
     * @return Created map
     */
    public static GameMap loadFromDatabase(SQLiteDatabase db, int width, int height) {
        GameMap map = new GameMap(db, height, width);

        MapElementCursor cursor = new MapElementCursor(db.query(MapElementTable.NAME,
                null, MapElementTable.Cols.GAME_ID + " = ?", new String[]{String.valueOf(GameData.DEFAULT_DATABASE_GAME_DATA_ID)} , null,
                null, null, null));

        //Add map elements from database into the map
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //Add the current map element
                cursor.addMapElement(map);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return map;
    }

    public MapElement getMapElement(int row, int col) {
        return map[row][col];
    }

    /**
     * Adds a structure to the map at a given location
     * @param structure The structure to add
     * @param row row position
     * @param col column position
     * @throws MapException if game rules prevent the structure being built
     */
    public void addStructure(Structure structure, int row, int col) throws MapException {
        //Throw exception if there is already a structure in the place structure is to be added
        if (map[row][col].getStructure() != null) {
            throw new MapException("Structure already exists here!");
        }

        //Throw exception if structure is not in a valid position
        if (!validateStructurePosition(structure, row, col)) {
            throw new MapException("This building cannot be built here!");
        }

        //Add structure to the map
        map[row][col].setStructure(structure);

        // Count structure
        structureAmounts.put(structure.getType(), structureAmounts.get(structure.getType()) + 1);

        //Add the map element to the database as having a structure
        addDatabaseMapElement(map[row][col]);
    }

    /** Adds a structure from the database. Does NOT attempt to store the structure in the database.
     * Also performs no additional validation to ensure accuracy between database and game representation.
     */
    public void addStructureFromDatabase(Structure structure, int rowPos, int colPos) throws MapException {
        map[rowPos][colPos].setStructure(structure);

        structureAmounts.put(structure.getType(), structureAmounts.get(structure.getType()) + 1);
    }


    public void demolishStructure(int row, int col) throws MapException {
        Structure structure = map[row][col].getStructure();

        //If there is a structure at the specified position
        if (structure != null) {
            //TODO don't allow demolishing of a road if it would cause adjacent buildings to be road-less

            map[row][col].removeStructure();

            //Remove 1 from structure count
            structureAmounts.put(structure.getType(), structureAmounts.get(structure.getType()) - 1);

            //Remove from the database
            removeDatabaseMapElement(map[row][col]);
        }
    }

    private boolean validateStructurePosition(Structure structure, int row, int col) {
        boolean validPosition = true;   //Position is valid by default (unless some rule invalidates
                                        // it)

        //Assert row and column called are within map boundaries
        if (!(row < map.length && row >= 0) || !(col < map[0].length && col >= 0)) {
            throw new IndexOutOfBoundsException("Element not within map bounds");
        }

        //** RULE FORCING RESIDENTIAL & COMMERCIAL TO BE NEXT TO ROAD **
        if (structure.getType() == Structure.Type.RESIDENTIAL ||
                structure.getType() == Structure.Type.COMMERCIAL) {
            validPosition = false;

            //Check if at least one of surrounding map elements contains a road-type structure
            int[] rowValues = new int[]{row + 1, row - 1, row, row};
            int[] colValues = new int[]{col, col, col + 1, col - 1};

            for (int ii = 0; ii < 4; ii++) {
                try {
                    MapElement checkElement = getMapElement(rowValues[ii], colValues[ii]);
                    Structure checkStructure = checkElement.getStructure();

                    if (checkStructure != null) {
                        if (checkElement.getStructure().getType() == Structure.Type.ROAD) {
                            validPosition = true;
                            break;
                        }
                    }
                } catch (IndexOutOfBoundsException i) {
                    // Do nothing (just don't check the map element that doesn't exist)
                }
            }
        }

        return validPosition;
    }

    public int getMapHeight() {
        return map.length;
    }

    public int getMapWidth() {
        return map[0].length;
    }

    /**
     * Initialises all components of the map to be empty background elements
     */
    private static MapElement[][] initialiseMap(int height, int width) {
        MapElement[][] newMap = new MapElement[height][width];

        for (int row = 0; row < newMap.length; row++) {
            for (int col = 0; col < newMap[row].length; col++) {
                newMap[row][col] = new MapElement(row, col);
            }
        }

        return newMap;
    }

    public int getStructureAmount(Structure.Type type) {
        return structureAmounts.get(type);
    }

    private void addDatabaseMapElement(MapElement mapElement) {
        ContentValues cv = mapElement.getContentValues(GameData.DEFAULT_DATABASE_GAME_DATA_ID);

        long result = db.insert(MapElementTable.NAME, null, cv);

        if (BuildConfig.DEBUG && result == -1) {
            throw new AssertionError("Database map element insert failed");
        }
    }

    private void removeDatabaseMapElement(MapElement mapElement) {
        String whereClause = MapElementTable.Cols.GAME_ID + " = ? AND " +
                MapElementTable.Cols.ROW + " = ? AND " +
                MapElementTable.Cols.COLUMN + " = ?";

        String[] whereArgs = new String[]{
                String.valueOf(GameData.DEFAULT_DATABASE_GAME_DATA_ID),
                String.valueOf(mapElement.getRowPos()),
                String.valueOf(mapElement.getColPos())
        };


        long result = db.delete(MapElementTable.NAME, whereClause, whereArgs);

        if (BuildConfig.DEBUG && result == -1) {
            throw new AssertionError("Database map element delete failed");
        }
    }

    /**
     * Updates a map element in the database when it is not created or destroyed, but changed (e.g.
     *  adding a name or bitmap image)
     */
    private void updateDatabaseMapElement(MapElement mapElement) {
        ContentValues cv = mapElement.getContentValues(GameData.DEFAULT_DATABASE_GAME_DATA_ID);

        //Make where clause for finding this specific map element
        String whereClause = MapElementTable.Cols.GAME_ID + " = ? AND " +
                MapElementTable.Cols.ROW + " = ? AND " +
                MapElementTable.Cols.COLUMN + " = ?";

        String[] whereArgs = new String[]{
                String.valueOf(GameData.DEFAULT_DATABASE_GAME_DATA_ID),
                String.valueOf(mapElement.getRowPos()),
                String.valueOf(mapElement.getColPos())
        };


        int affectedRows = db.update(MapElementTable.NAME, cv, whereClause, whereArgs);

        if (BuildConfig.DEBUG && affectedRows == 0) {
            throw new AssertionError("Database map element update failed");
        }
    }

    public void setElementOwnerName(String ownerName, int row, int col) {
        map[row][col].setOwnerName(ownerName);

        //Update map element's database entry with new information
        updateDatabaseMapElement(map[row][col]);
    }

    public void setElementSpecialImage(Bitmap specialImage, int row, int col) {
        map[row][col].setSpecialImage(specialImage);

        //Update map element's database entry with new information
        updateDatabaseMapElement(map[row][col]);
    }
}