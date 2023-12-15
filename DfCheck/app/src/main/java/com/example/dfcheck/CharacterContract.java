package com.example.dfcheck;

import android.provider.BaseColumns;

// CharacterContract.java
public final class CharacterContract {
    private CharacterContract() {}

    public static class CharacterEntry implements BaseColumns {
        public static final String TABLE_NAME = "characters";
        public static final String COLUMN_SERVER_ID = "serverId";
        public static final String COLUMN_CHARACTER_ID = "characterId";
        public static final String COLUMN_CHARACTER_NAME = "characterName";
        public static final String COLUMN_JOB_NAME = "jobName";
        public static final String COLUMN_JOB_GROW_NAME = "jobGrowName";
        public static final String COLUMN_FAME = "fame";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_SERVER_ID + " TEXT," +
                        COLUMN_CHARACTER_ID + " TEXT," +
                        COLUMN_CHARACTER_NAME + " TEXT," +
                        COLUMN_JOB_NAME + " TEXT," +
                        COLUMN_JOB_GROW_NAME + " TEXT," +
                        COLUMN_FAME + " INTEGER)";
    }
}
