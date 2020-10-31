package com.moritzbergemann.myapplication.database;

public class DatabaseSchema {
    public static class SettingsTable {
        public static final String NAME = "settings";

        public static class Cols {
            public static final String ID = "setting_id";
            public static final String INITIAL_MONEY = "initial_money";
            public static final String MAP_WIDTH = "map_width";
            public static final String MAP_HEIGHT = "map_height";
            public static final String CITY_NAME = "city_name";
        }
    }

    public static class GamesTable {
        public static final String NAME = "games";

        public static class Cols {
            public static final String ID = "game_id";
            public static final String MONEY = "money";
            public static final String TIME = "time";
            public static final String GAME_STARTED = "game_started";
        }
    }

    public static class MapElementTable {
        public static final String NAME = "map_elements";

        public static class Cols {
            public static final String GAME_ID = "game_id"; //NOTE: This is not a foreign key as there is only one game, but could easily be updated to become one in the future
            public static final String STRUCTURE_TYPE_ID = "structure_type_id";
            public static final String OWNER_NAME = "owner_name";
            public static final String IMAGE_BITMAP = "image_bitmap";
            public static final String ROW = "element_row";
            public static final String COLUMN = "element_column";
        }
    }
}
