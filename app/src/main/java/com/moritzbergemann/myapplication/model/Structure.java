package com.moritzbergemann.myapplication.model;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a type of structure in the game. Possesses a type and an image representing
 * the structure. This class should be cloned whenever a new instance of the structure is added
 * to the map.
 */
public class Structure {
    public enum Type {
        RESIDENTIAL,
        COMMERCIAL,
        ROAD
    }

    private String id; //Unique identifier for this type of structure
    private int imageId;
    private Type type;

    public Structure(String id, int imageId, Type type) {
        this.id = id;
        this.imageId = imageId;
        this.type = type;
    }

    public Structure(Structure structure) {
        this.imageId = structure.imageId;
        this.type = structure.type;
    }

    public Structure clone() {
        return new Structure(this);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean equal = false;

        if (obj instanceof Structure) {
            Structure structure = (Structure) obj;
            if (structure.imageId == this.imageId && structure.type.equals(this.type)) {
                equal = true;
            }
        }

        return equal;
    }

    public int getImageId() {
        return imageId;
    }

    public Type getType() {
        return type;
    }

    public static String getTypeName(Type type) {
        Map<Type, String> typeNames = new HashMap<>();
        typeNames.put(Type.RESIDENTIAL, "Residential");
        typeNames.put(Type.COMMERCIAL, "Commercial");
        typeNames.put(Type.ROAD, "Road");

        return typeNames.get(type);
    }
}
