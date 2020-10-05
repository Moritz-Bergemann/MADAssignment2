package com.moritzbergemann.myapplication.model;

import java.util.LinkedList;
import java.util.List;

public class MapData {
    private static MapData instance;

    public static MapData get() {
        if (instance == null) {
            instance = new MapData();
        }

        return instance;
    }

    // Fields
    private MapElement[][] map;
    private List<Residential> residential;
    private List<Commercial> commercial;
    private List<Road> roads;

    private MapData() {
        Settings settings = GameData.get().getSettings();

        map = new MapElement[settings.getMapWidth()][settings.getMapHeight()];
        residential = new LinkedList<>();
        commercial = new LinkedList<>();
        roads = new LinkedList<>();
    }

    public List<Residential> getResidential() {
        return residential;
    }

    public List<Commercial> getCommercial() {
        return commercial;
    }

    public List<Road> getRoads() {
        return roads;
    }
}
