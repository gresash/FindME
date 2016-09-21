package com.example.ahmet.findme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DbHelper extends SQLiteOpenHelper {
     static class FieldEntry implements BaseColumns {
        public static final String TABLE_NAME = "LocationLogs";
        public static final String COLUMN_NAME_LOCATION = "Location";
        public static final String COLUMN_NAME_TEMPERATURE = "Temperature";
        public static final String COLUMN_NAME_TIMESTAMP = "Timestamp";
        public static final String COLUMN_SPACE="    ";
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID+" INTEGER PRIMARY KEY"+COMMA_SEP+
                        COLUMN_NAME_TIMESTAMP + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TEMPERATURE + TEXT_TYPE + " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    // Insert the new row, returning the primary key value of the new row

    public  long insertData(String sLocation,String sTemperature) {
        // Gets the data repository in write mode

        ContentValues content = new ContentValues();
        content.put("Location",sLocation);
        content.put("Temperature", sTemperature);
        content.put("Timestamp", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH).format(new Date()));
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.insert(FieldEntry.TABLE_NAME, null, content);
        return newRowId;
    }

    public  String readData() {
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        SQLiteDatabase db = this.getReadableDatabase();


            String[] projection = {
                    FieldEntry.COLUMN_NAME_LOCATION,
                    FieldEntry.COLUMN_NAME_TEMPERATURE,
                    FieldEntry.COLUMN_NAME_TIMESTAMP
            };

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    FieldEntry._ID + " DESC";


            Cursor c = db.query(
                    FieldEntry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null,
                    null,
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            if (c.getCount()>0) {
                String data = "TIME"+FieldEntry.COLUMN_SPACE+"LOCATION"+FieldEntry.COLUMN_SPACE+"TEMPERATURE(c)\n";
                c.moveToFirst();
                while(!c.isAfterLast()) {


                    data += c.getString(c.getColumnIndexOrThrow(FieldEntry.COLUMN_NAME_TIMESTAMP)) + FieldEntry.COLUMN_SPACE +
                            c.getString(c.getColumnIndexOrThrow(FieldEntry.COLUMN_NAME_LOCATION)) +  FieldEntry.COLUMN_SPACE +
                            c.getString(c.getColumnIndexOrThrow(FieldEntry.COLUMN_NAME_TEMPERATURE)) + " degrees\n";


                    c.moveToNext();
                }
                c.close();
                return data;
                }
                return "";
    }



        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Gresa_Assignment1.db";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(FieldEntry.SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(FieldEntry.SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

