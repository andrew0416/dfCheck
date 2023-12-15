package com.example.dfcheck;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CharacterDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "character_database";
    private static final int DATABASE_VERSION = 1;

    public CharacterDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스가 생성될 때 호출됨
        db.execSQL(CharacterContract.CharacterEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 버전이 변경될 때 호출됨
        // 여기서는 단순히 테이블을 삭제하고 다시 생성함
        db.execSQL("DROP TABLE IF EXISTS " + CharacterContract.CharacterEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Character ID가 이미 존재하는지 확인
        if (!isCharacterIdExists(character.getCharacterId())) {
            // Character ID가 존재하지 않으면 추가
            ContentValues values = new ContentValues();
            values.put(CharacterContract.CharacterEntry.COLUMN_SERVER_ID, character.getServerId());
            values.put(CharacterContract.CharacterEntry.COLUMN_CHARACTER_ID, character.getCharacterId());
            values.put(CharacterContract.CharacterEntry.COLUMN_CHARACTER_NAME, character.getCharacterName());
            values.put(CharacterContract.CharacterEntry.COLUMN_JOB_NAME, character.getJobName());
            values.put(CharacterContract.CharacterEntry.COLUMN_JOB_GROW_NAME, character.getJobGrowName());
            values.put(CharacterContract.CharacterEntry.COLUMN_FAME, character.getFame());

            db.insert(CharacterContract.CharacterEntry.TABLE_NAME, null, values);
            Log.d("CharacterDB", "New character added with ID: " + character.getCharacterId());
        }

        db.close();
    }

    private boolean isCharacterIdExists(String characterId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + CharacterContract.CharacterEntry.TABLE_NAME +
                " WHERE " + CharacterContract.CharacterEntry.COLUMN_CHARACTER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{characterId});

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        return exists;
    }

    public void deleteAllCharacters() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CharacterContract.CharacterEntry.TABLE_NAME, null, null);
        db.close();
        Log.d("CharacterDB", "All characters deleted");
    }

    @SuppressLint("Range")
    public List<Character> getAllCharacters() {
        List<Character> characters = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CharacterContract.CharacterEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Character character = new Character();
                character.setServerId(cursor.getString(cursor.getColumnIndex(CharacterContract.CharacterEntry.COLUMN_SERVER_ID)));
                character.setCharacterId(cursor.getString(cursor.getColumnIndex(CharacterContract.CharacterEntry.COLUMN_CHARACTER_ID)));
                character.setCharacterName(cursor.getString(cursor.getColumnIndex(CharacterContract.CharacterEntry.COLUMN_CHARACTER_NAME)));
                character.setJobName(cursor.getString(cursor.getColumnIndex(CharacterContract.CharacterEntry.COLUMN_JOB_NAME)));
                character.setJobGrowName(cursor.getString(cursor.getColumnIndex(CharacterContract.CharacterEntry.COLUMN_JOB_GROW_NAME)));
                character.setFame(cursor.getInt(cursor.getColumnIndex(CharacterContract.CharacterEntry.COLUMN_FAME)));

                characters.add(character);
            } while (cursor.moveToNext());
        }

        Log.d("CharacterDB", "Number of characters retrieved: " + characters.size());

        cursor.close();
        db.close();
        return characters;
    }
    public void deleteCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CharacterContract.CharacterEntry.TABLE_NAME,
                CharacterContract.CharacterEntry.COLUMN_CHARACTER_ID + " = ?",
                new String[]{character.getCharacterId()});
        db.close();
        Log.d("CharacterDB", "Character deleted with ID: " + character.getCharacterId());
    }
}
