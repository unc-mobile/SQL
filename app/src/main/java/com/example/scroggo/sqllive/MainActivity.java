package com.example.scroggo.sqllive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DbHelper mDatabase;

    boolean firstTime = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = new DbHelper(this);

        if (firstTime) {
            ContentValues john = new ContentValues();
            john.put(CharactersContract.CharactersTable.COLUMN_NAME_NAME,
                    "John");
            john.put(CharactersContract.CharactersTable.COLUMN_NAME_HOME,
                    "Winterfell");

            ContentValues tyrion = new ContentValues();
            tyrion.put(CharactersContract.CharactersTable.COLUMN_NAME_NAME,
                    "Tyrion");
            tyrion.put(CharactersContract.CharactersTable.COLUMN_NAME_HOME,
                    "Casterly Rock");

            ContentValues daenerys = new ContentValues();
            daenerys.put(CharactersContract.CharactersTable.COLUMN_NAME_NAME,
                    "Daenerys");
            daenerys.put(CharactersContract.CharactersTable.COLUMN_NAME_HOME,
                    "Dragon Stone");

            new Inserter(CharactersContract.CharactersTable.TABLE_NAME).execute(
                    john, tyrion, daenerys);

            List<ContentValues> list = new ArrayList<>();

            for (int[] array : new int[][]{
                    {1, 1, 10},
                    {1, 2, 5},
                    {1, 3, 7},
                    {2, 1, 5},
                    {2, 2, 5},
                    {2, 3, 9},
                    {3, 1, 10},
                    {3, 2, 2}
            }) {
                ContentValues values = new ContentValues();
                values.put(CharactersContract.AppearanceTable.COLUMN_NAME_EPISODE,
                        array[0]);
                values.put(CharactersContract.AppearanceTable.COLUMN_NAME_CHARACTER_ID,
                        array[1]);
                values.put(CharactersContract.AppearanceTable.COLUMN_NAME_APPEARED,
                        array[2]);
                list.add(values);
            }

            new Inserter(CharactersContract.AppearanceTable.TABLE_NAME).execute(
                    list.toArray(new ContentValues[0]));
        }

        new Joiner().execute();
    }

    private class Joiner extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPostExecute(Cursor cursor) {
            printCursor(cursor);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            SQLiteDatabase db = mDatabase.getReadableDatabase();
            String sql = "SELECT * FROM " + CharactersContract.CharactersTable.TABLE_NAME + ", "
                    + CharactersContract.AppearanceTable.TABLE_NAME + " WHERE "
                    + CharactersContract.CharactersTable._ID + " = "
                    + CharactersContract.AppearanceTable.COLUMN_NAME_CHARACTER_ID + " AND "
                    + CharactersContract.AppearanceTable.COLUMN_NAME_EPISODE + " = 1 AND "
                    + CharactersContract.AppearanceTable.COLUMN_NAME_APPEARED + " < 10;";
            return db.rawQuery(sql, null);
        }
    }

    private void log(String msg) {
        Log.d("SCROGGO", msg);
    }

    private class Inserter extends AsyncTask<ContentValues, Void, Cursor> {
        String mTableName;

        public Inserter(String name) {
            mTableName = name;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(MainActivity.this, "No data!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            printCursor(cursor);
        }

        @Override
        protected Cursor doInBackground(ContentValues... contentValues) {
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            for (ContentValues contentValues1 : contentValues) {
                db.insert(mTableName,
                        null, contentValues1);
            }

/*
            // Delete rows with ID > 3
            String deleteSelection = CharactersContract.CharactersTable._ID
                    + " > ?";
            String[] deleteSelectionArgs = new String[] { "3" };
            int rowsDeleted = db.delete(CharactersContract.CharactersTable.TABLE_NAME,
                    deleteSelection, deleteSelectionArgs);
            log(rowsDeleted + " were deleted");
*/

            db = mDatabase.getReadableDatabase();
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = null;
            return db.query(mTableName,
                    projection, selection, selectionArgs,
                    null, null, sortOrder);
        }
    }

    private void printCursor(Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < columnCount; ++i) {
            line.append(cursor.getColumnName(i));
            if (i < columnCount - 1) {
                line.append("\t");
            }
        }

        log(line.toString());

        while (cursor.moveToNext()) {
            line = new StringBuilder();
            for (int i = 0; i < columnCount; ++i) {
                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        line.append(cursor.getInt(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        line.append(cursor.getString(i));
                        break;
                    default:
                        // do nothing
                        break;
                }
                if (i < columnCount - 1) {
                    line.append("\t");
                }
            }
            log(line.toString());
        }
    }
}
