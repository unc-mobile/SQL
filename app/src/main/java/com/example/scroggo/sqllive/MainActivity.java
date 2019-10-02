package com.example.scroggo.sqllive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DbHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = new DbHelper(this);

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

        new Inserter().execute(john, tyrion, daenerys);
    }

    private void log(String msg) {
        Log.d("SCROGGO", msg);
    }

    private class Inserter extends AsyncTask<ContentValues, Void, Cursor> {
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(MainActivity.this, "No data!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

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

        @Override
        protected Cursor doInBackground(ContentValues... contentValues) {
            SQLiteDatabase db = mDatabase.getWritableDatabase();
            for (ContentValues contentValues1 : contentValues) {
                db.insert(CharactersContract.CharactersTable.TABLE_NAME,
                        null, contentValues1);
            }

            db = mDatabase.getReadableDatabase();
            String[] projection = null;
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = null;
            return db.query(CharactersContract.CharactersTable.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, sortOrder);
        }
    }
}
