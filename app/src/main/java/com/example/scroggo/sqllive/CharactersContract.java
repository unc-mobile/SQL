package com.example.scroggo.sqllive;

import android.provider.BaseColumns;

public class CharactersContract {
    private CharactersContract(){}

    public static class CharactersTable implements BaseColumns {
        public static final String TABLE_NAME = "characters";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_HOME = "home";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_NAME + " TEXT, "
                + COLUMN_NAME_HOME + " TEXT)";
    }

    public static class AppearanceTable implements BaseColumns {
        public static final String TABLE_NAME = "appearance";
        public static final String COLUMN_NAME_EPISODE = "episode";
        public static final String COLUMN_NAME_CHARACTER_ID = "characterID";
        public static final String COLUMN_NAME_APPEARED = "appeared";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_EPISODE + " INTEGER, "
                + COLUMN_NAME_CHARACTER_ID + " INTEGER, "
                + COLUMN_NAME_APPEARED + " INTEGER)";
    }
}
