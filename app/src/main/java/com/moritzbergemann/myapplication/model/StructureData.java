package com.moritzbergemann.myapplication.model;

import com.moritzbergemann.myapplication.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StructureData {
    private static StructureData instance = null;

    public static StructureData get() {
        if (instance == null) {
            instance = new StructureData();
        }

        return instance;
    }

    private Map<String, Structure> structureTypes;

    private StructureData() {
        structureTypes = setupStructureTypes();
    }

    // NOTE - Currently structures are accessed by list element index. For future extensibility
    //  (i.e. adding more structures) this should be changed to a dictionary.

    public Map<String, Structure> getStructureTypes() {
        return structureTypes;
    }

    private static Map<String, Structure> setupStructureTypes() {
        Map<String, Structure> map = new HashMap<>();

        Settings settings = GameData.get().getSettings();

        map.put("residential1", new Structure("residential1", R.drawable.ic_residential1, Structure.Type.RESIDENTIAL));
        map.put("residential2", new Structure("residential2", R.drawable.ic_residential2, Structure.Type.RESIDENTIAL));
        map.put("residential3", new Structure("residential3", R.drawable.ic_residential3, Structure.Type.RESIDENTIAL));
        map.put("residential4", new Structure("residential4", R.drawable.ic_residential4, Structure.Type.RESIDENTIAL));
        map.put("industrial1", new Structure("industrial1", R.drawable.ic_industrial1, Structure.Type.COMMERCIAL));
        map.put("industrial2", new Structure("industrial2", R.drawable.ic_industrial2, Structure.Type.COMMERCIAL));
        map.put("industrial3", new Structure("industrial3", R.drawable.ic_industrial3, Structure.Type.COMMERCIAL));
        map.put("industrial4", new Structure("industrial4", R.drawable.ic_industrial4, Structure.Type.COMMERCIAL));
        map.put("road_e", new Structure("road_e", R.drawable.ic_road_e, Structure.Type.ROAD));
        map.put("road_ew", new Structure("road_ew", R.drawable.ic_road_ew, Structure.Type.ROAD));
        map.put("road_n", new Structure("road_n", R.drawable.ic_road_n, Structure.Type.ROAD));
        map.put("road_ne", new Structure("road_ne", R.drawable.ic_road_ne, Structure.Type.ROAD));
        map.put("road_new", new Structure("road_new", R.drawable.ic_road_new, Structure.Type.ROAD));
        map.put("road_ns", new Structure("road_ns", R.drawable.ic_road_ns, Structure.Type.ROAD));
        map.put("road_nse", new Structure("road_nse", R.drawable.ic_road_nse, Structure.Type.ROAD));
        map.put("road_nsew", new Structure("road_nsew", R.drawable.ic_road_nsew, Structure.Type.ROAD));
        map.put("road_nsw", new Structure("road_nsw", R.drawable.ic_road_nsw, Structure.Type.ROAD));
        map.put("road_nw", new Structure("road_nw", R.drawable.ic_road_nw, Structure.Type.ROAD));
        map.put("road_s", new Structure("road_s", R.drawable.ic_road_s, Structure.Type.ROAD));
        map.put("road_se", new Structure("road_se", R.drawable.ic_road_se, Structure.Type.ROAD));
        map.put("road_sew", new Structure("road_sew", R.drawable.ic_road_sew, Structure.Type.ROAD));
        map.put("road_sw", new Structure("road_sw", R.drawable.ic_road_sw, Structure.Type.ROAD));
        map.put("road_w", new Structure("road_w", R.drawable.ic_road_w, Structure.Type.ROAD));

        return map;
    }
}
