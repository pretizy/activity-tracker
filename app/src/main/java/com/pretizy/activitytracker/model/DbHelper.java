package com.pretizy.activitytracker.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by gerald on 10/5/16.
 */
public abstract class DbHelper<T> extends SQLiteOpenHelper{
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EventReaderContract.EventEntry.TABLE_NAME + " (" +
                    EventReaderContract.EventEntry._ID + " INTEGER PRIMARY KEY," +
                    EventReaderContract.EventEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    EventReaderContract.EventEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    EventReaderContract.EventEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    EventReaderContract.EventEntry.COLUMN_NAME_AM_PM + TEXT_TYPE + COMMA_SEP +
                    EventReaderContract.EventEntry.COLUMN_NAME_TIME + TEXT_TYPE+ " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EventReaderContract.EventEntry.TABLE_NAME;
    private static final String DATABASE_NAME = "ActivityTracker.db";
    private static final int DATABASE_VERSION = 3;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long create(ContentValues values){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        return db.insert(getTableName(), null, values);

    }

    public Cursor findAll(){
        SQLiteDatabase db = getReadableDatabase();
        // Filter results WHERE "title" = 'My Title'
       Cursor c = db.query(
                getTableName(),                     // The table to query
                null,                               // The columns to return
                null,                               // The columns for the WHERE clause
                null,                               // The values for the WHERE clause
                null,                               // don't group the rows
                null,                               // don't filter by row groups
                null                                // The sort order
        );
        return c;
    }


    public boolean delete(String id){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(getTableName(), BaseColumns._ID+"=?", new String[]{id})>0;
    }

    abstract String getTableName();

    public Cursor findById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + getTableName() + " where " + BaseColumns._ID + "='" + id + "'" , null);
    }
}
