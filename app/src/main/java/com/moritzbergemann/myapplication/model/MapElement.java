package com.moritzbergemann.myapplication.model;

import android.content.ContentValues;
import android.graphics.Bitmap;

import com.moritzbergemann.myapplication.R;
import com.moritzbergemann.myapplication.database.DatabaseSchema.MapElementTable;

import java.io.ByteArrayOutputStream;

//Parts of this file consist of externally obtained code.

/**
 * Class representing a single map 'element' (space on the map grid).
 */
public class MapElement {
    private static int DEFAULT_BACKGROUND_IMAGE_RESOURCE = R.drawable.ic_grass_background;

    private Structure structure;
    private Bitmap specialImage;
    private String ownerName; //Name given to this map element by user
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

    /**
     * Setter for structure. Does NOT perform validation, database saves etc
     */
    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    /**
     * Setter for special image. Does NOT perform database saves etc
     */
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
     * Remove the structure currently here from this map element. This also removes the special
     * image and name.
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

    /**
     * @param gameId the ID of the game this map element belongs to
     * @return A ContentValues object for storing a map element in a database
     */
    public ContentValues getContentValues(int gameId) {
        ContentValues cv = new ContentValues();

        cv.put(MapElementTable.Cols.GAME_ID, gameId);
        cv.put(MapElementTable.Cols.ROW, rowPos);
        cv.put(MapElementTable.Cols.COLUMN, colPos);
        cv.put(MapElementTable.Cols.STRUCTURE_TYPE_ID, structure.getId());
        cv.put(MapElementTable.Cols.OWNER_NAME, ownerName);

        //Add special image (if it exists)
        if (specialImage != null) {
            // Externally obtained code: retrieved from https://stackoverflow.com/a/17191158
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            specialImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            cv.put(MapElementTable.Cols.IMAGE_BITMAP, byteArray);
        } else {
            cv.put(MapElementTable.Cols.IMAGE_BITMAP, (byte[])null);
        }

        return cv;
    }
}