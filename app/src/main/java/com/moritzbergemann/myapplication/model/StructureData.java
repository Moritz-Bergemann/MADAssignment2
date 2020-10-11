package com.moritzbergemann.myapplication.model;

import com.moritzbergemann.myapplication.R;

import java.util.LinkedList;
import java.util.List;

public class StructureData {
    private static StructureData instance = null;

    public static StructureData get() {
        if (instance == null) {
            instance = new StructureData();
        }

        return instance;
    }

    private List<Structure> structureTypes;

    private StructureData() {
        structureTypes = setupStructureTypes();
    }

    public List<Structure> getStructureTypes() {
        return structureTypes;
    }

    private static List<Structure> setupStructureTypes() {
        List<Structure> list = new LinkedList<>();

        Settings settings = GameData.get().getSettings();

        list.add(new Structure(R.drawable.ic_residential1, Structure.Type.RESIDENTIAL));
        list.add(new Structure(R.drawable.ic_residential2, Structure.Type.RESIDENTIAL));
        list.add(new Structure(R.drawable.ic_residential3, Structure.Type.RESIDENTIAL));
        list.add(new Structure(R.drawable.ic_residential4, Structure.Type.RESIDENTIAL));
        list.add(new Structure(R.drawable.ic_industrial1, Structure.Type.COMMERCIAL));
        list.add(new Structure(R.drawable.ic_industrial2, Structure.Type.COMMERCIAL));
        list.add(new Structure(R.drawable.ic_industrial3, Structure.Type.COMMERCIAL));
        list.add(new Structure(R.drawable.ic_industrial4, Structure.Type.COMMERCIAL));
        list.add(new Structure(R.drawable.ic_road_e, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_ew, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_n, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_ne, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_new, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_ns, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_nse, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_nsew, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_nsw, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_nw, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_s, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_se, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_sew, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_sw, Structure.Type.ROAD));
        list.add(new Structure(R.drawable.ic_road_w, Structure.Type.ROAD));

        return list;
    }
}
