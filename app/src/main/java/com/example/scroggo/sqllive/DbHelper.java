package com.example.scroggo.sqllive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    // VERSION 2 adds the appearance table
    // VERSION 3 adds the _ID column to the AppearanceTable
    // VERSION 4 removes it, since we need to change our query to support it
    private static final int VERSION = 4;
    private static final String NAME = "characters.db";

    public DbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                CharactersContract.CharactersTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(
                CharactersContract.AppearanceTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            sqLiteDatabase.execSQL(CharactersContract.AppearanceTable.CREATE_TABLE);
            return;
        }
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + CharactersContract.CharactersTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + CharactersContract.AppearanceTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
