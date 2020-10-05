package com.moritzbergemann.myapplication.model;

import android.graphics.Bitmap;

public class MapElement {
    private Structure structure;
    private Bitmap image;
    private String ownerName;
    private Bitmap backgroundImage;

    public MapElement(Structure structure, Bitmap image, String ownerName) {
        this.structure = structure;
        this.image = image;
        this.ownerName = ownerName;
    }

    public Structure getStructure() {
        return structure;
    }
}
