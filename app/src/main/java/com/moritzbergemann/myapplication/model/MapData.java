package com.moritzbergemann.myapplication.model;

import java.util.LinkedList;
import java.util.List;

public class MapData {
    //TODO make the city not a dictatorship
    private static final String OWNER_NAME = "God-Emperor Moritz";

    private static MapData instance;

    public static MapData get() {
        if (instance == null) {
            instance = new MapData();
        }

        return instance;
    }

    // Fields
    private MapElement[][] map;
    private List<Structure> residentialList;
    private List<Structure> commercialList;
    private List<Structure> roadList;

    private MapData() {
        Settings settings = GameData.get().getSettings();

        map = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        residentialList = new LinkedList<>();
        commercialList = new LinkedList<>();
        roadList = new LinkedList<>();
    }

    public MapElement getMapElement(int row, int col) {
        return map[row][col];
    }

    public void addStructure(Structure structure, int row, int col) {
        //Try to add the submission to the database (will throw exception if there's already
        // something there)
        MapElement mapElement = new MapElement(structure, null, OWNER_NAME);
        addMapElement(mapElement, row, col);

        // Add structure to the list of its given type
        trackStructure(structure);
    }

    private void addMapElement(MapElement mapElement, int row, int col) {
        //Throw exception if there is already a structure in the place structure is to be added
        if (map[row][col] != null) {
            throw new IllegalArgumentException("Structure already exists here!");
        }

        map[row][col] = mapElement;

        //TODO add shizzle to the database
    }

    private void trackStructure(Structure structure) {
        switch (structure.getType()) {
            case RESIDENTIAL:
                residentialList.add(structure);
                break;
            case COMMERCIAL:
                commercialList.add(structure);
                break;
            case ROAD:
                roadList.add(structure);
                break;
            default:
                throw new IllegalArgumentException("Unexpected structure type");
        }
    }

    public int getMapHeight() {
        return map.length;
    }

    public int getMapWidth() {
        return map[0].length;
    }

    // ACCESSORS FOR DIFFERENT STRUCTURES FIXME are these even necessary?
    public List<Structure> getResidentialList() {
        return residentialList;
    }

    public List<Structure> getCommercialList() {
        return commercialList;
    }

    public List<Structure> getRoadList() {
        return roadList;
    }

    //  TODO remove this - old way of doing structures
//
//    //Adding different structures to the map
//    public void addResidential(int row, int col) {
//        Residential newRes = new Residential();
//
//        addMapElement(new MapElement(newRes, ));
//
//        //Insert the structure
//        residentialList.add(newRes);
//    }
//
//    public void addCommercial(int row, int col) {
//        Commercial newCom = new Commercial();
//
//        //Insert the structure
//        commercialList.add(newCom);
//    }
//
//    public void addRoad(int row, int col) {
//        Road newRoad = new Road();
//
//        //Insert the structure
//        roadList.add(newRoad);
//    }

}
