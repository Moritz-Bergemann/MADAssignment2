package com.moritzbergemann.myapplication.model;

import java.util.LinkedList;
import java.util.List;

public class GameMap {
    // Fields
    private MapElement[][] map;
    private List<Structure> structureList; //FIXME also possibly unecessary (or change to eliminate lists and only make counter for each type)

    public GameMap(int height, int width) {
        map = initialiseMap(height, width);
        structureList = new LinkedList<>();
    }

    public MapElement getMapElement(int row, int col) {
        return map[row][col];
    }

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

        // Add structure to the list of its given type
        structureList.add(structure);;

        //TODO add shizzle to the database
    }

    public void demolishStructure(int row, int col) throws MapException {
        //If there is a structure at the specified position
        if (getMapElement(row, col).getStructure() != null) {
            map[row][col].removeStructure();
            //TODO don't allow demolishing of a road if it would cause adjacent buildings to be road-less
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
        int numStructures = 0;

        for (Structure structure : structureList) {
            if (structure.getType() == type) {
                numStructures++;
            }
        }
        return numStructures;
    }
}