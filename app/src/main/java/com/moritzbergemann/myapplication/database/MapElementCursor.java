package com.moritzbergemann.myapplication.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.moritzbergemann.myapplication.model.GameMap;
import com.moritzbergemann.myapplication.database.DatabaseSchema.MapElementTable;
import com.moritzbergemann.myapplication.model.MapException;
import com.moritzbergemann.myapplication.model.Structure;
import com.moritzbergemann.myapplication.model.StructureData;

import java.io.ByteArrayInputStream;

public class MapElementCursor extends CursorWrapper {
    public MapElementCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Adds the contents of a map element to the imported map
     * @param map map to add element to
     */
    public void addMapElement(GameMap map) {
        //Getting information from database
        int rowPos = getInt(getColumnIndex(MapElementTable.Cols.ROW));
        int colPos = getInt(getColumnIndex(MapElementTable.Cols.COLUMN));

        String structureTypeId = getString(getColumnIndex(MapElementTable.Cols.STRUCTURE_TYPE_ID));
        String ownerName = getString(getColumnIndex(MapElementTable.Cols.OWNER_NAME));

        byte[] customImageBlob = getBlob(getColumnIndex(MapElementTable.Cols.IMAGE_BITMAP));

        //Processing info
        Bitmap specialImage = null;
        if (customImageBlob != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(customImageBlob);
            specialImage = BitmapFactory.decodeStream(imageStream);
        }
        Structure structure = StructureData.get().getStructureTypes().get(structureTypeId);

        //Adding info to map
        try {
            //Add structure to map (to ensure it gets counted)
            map.addStructureFromDatabase(structure, rowPos, colPos);

            map.getMapElement(rowPos, colPos).setOwnerName(ownerName);

            if (specialImage != null) {
                map.getMapElement(rowPos, colPos).setSpecialImage(specialImage);
            }
        } catch (MapException m) {  //If structure cannot be added (means something DB-related
                                    // messed up)
            throw new IllegalArgumentException("Could not add structure from database - " +
                    m.getMessage());
        }
    }
}
