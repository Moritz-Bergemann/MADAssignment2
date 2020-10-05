package com.moritzbergemann.myapplication.database;

public class DatabaseSchema {
    public static class SettingsTable {
        public static final String NAME = "settings";

        public static class Cols {
            public static final String ID = "setting_id";
            public static final String GAME = "game"; //FIXME FKs not implemented, see if you need them later
            public static final String MAP_WIDTH = "map_width";
            public static final String MAP_HEIGHT = "map_height";
        }
    }

    public static class GamesTable {
        public static final String NAME = "games";

        public static class Cols {
            public static final String ID = "game_id";
            public static final String CITY_NAME = "city_name";
            public static final String MONEY = "money";
            public static final String TIME = "time";
        }
    }

    public static class StructuresTable {
        public static final String NAME = "structures";

        public static class Cols {
            public static final String ID = "structure_ID";
            public static final String GAME = "game_id"; //FIXME FKs not implemented, see if you need them later
            public static final String TYPE = "type";
            public static final String ROW = "row";
            public static final String COLUMN = "column";
        }
    }
}
