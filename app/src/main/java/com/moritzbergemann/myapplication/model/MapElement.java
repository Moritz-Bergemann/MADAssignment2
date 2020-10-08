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
        this.specialImage = null;
        this.ownerName = "Nature";
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
}