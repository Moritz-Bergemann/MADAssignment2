package com.moritzbergemann.myapplication.model;

import android.graphics.Bitmap;

import com.moritzbergemann.myapplication.R;

public class MapElement {
    private static int DEFAULT_BACKGROUND_IMAGE_RESOURCE = R.drawable.ic_grass_background;

    private Structure structure;
    private Bitmap specialImage;
    private String ownerName;
    private int backgroundImageResource;
    private int rowPos;
    private int colPos;

    public MapElement(int rowPos, int colPos) {
        this.rowPos = rowPos;
        this.colPos = colPos;
        this.structure = null;
        this.specialImage = null; //Special image set for this structure by user
        this.ownerName = null; //Unique name given to this structure by user
        this.backgroundImageResource = DEFAULT_BACKGROUND_IMAGE_RESOURCE;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public void setSpecialImage(Bitmap specialImage) {
        this.specialImage = specialImage;
    }

    public int getBackgroundImageResource() {
        return backgroundImageResource;
    }

    public int getRowPos() {
        return rowPos;
    }

    public int getColPos() {
        return colPos;
    }

    /**
     * Remove the structure currently here from this map element
     */
    public void removeStructure() {
        if (structure != null) {
            structure = null;
            specialImage = null;
            ownerName = null;
        }
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Bitmap getSpecialImage() {
        return specialImage;
    }
}