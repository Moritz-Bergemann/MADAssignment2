package com.moritzbergemann.myapplication.model;

/**
 * Class representing a type of structure in the game. Possesses a type and an image representing
 *  the structure. This class should be cloned whenever a new instance of the structure is added
 *  to the map.
 */
public class Structure {
    public enum Type {
        RESIDENTIAL,
        COMMERCIAL,
        ROAD
    }

    private int imageId;
    private Type type;
    private int cost;

    public Structure(int imageId, Type type, int cost) {
        this.imageId = imageId;
        this.type = type;
        this.cost = cost;
    }

    public int getImageId() {
        return imageId;
    }

    public Type getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }
}
