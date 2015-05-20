package com.androidmagic.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Class to manage all Db related operations.
 *
 * @Author : rohit Jun 15, 2013 7:25:06 PM
 */
public class DbManager {

    private static final String TAG = DbManager.class.getSimpleName();

    public static final String DB_NAME = "PersonDb";
    public static final String TABLE_NAME = "Person";

    public static final String DB_SCHEMA_RES = "db_schema.sql";
    public static final int DB_VERSION = 1;

    private static SQLiteOpenHelper mDbHelper;

    public static SQLiteOpenHelper init(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new ResourceBasedSQLiteOpenHelper(
                            context, DB_NAME, DB_VERSION, DB_SCHEMA_RES);
        }
        return mDbHelper;
    }

    /**
     * The id it will return should be used as local id of person.
     * @param person
     * @return
     */
    public static Long insertPersonIntoDb(Person person, Context context) {
        Long rowId = -1L;
        init(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues specVals = new ContentValues(10);
            specVals.put("name", person.name);
            specVals.put("address", person.address);
            
            rowId = db.insert(TABLE_NAME, null, specVals);
            Log.v(TAG, "Inserting Address " + specVals);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        person.id = rowId;
        return rowId;
    }

    /**
     * Update the Person object in database.
     * @param location
     */
    public static void updateAddressIntoDb(Person person, Context context) {
        init(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues specVals = new ContentValues(9);
            specVals.put("name", person.name);
            specVals.put("address", person.address);

            db.update(TABLE_NAME, specVals, "id = ?",
                            new String[] { String.valueOf(person.id) });
            db.setTransactionSuccessful();
            Log.v(TAG, "Updating Person " + specVals);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Delete Address from database.
     * @param location
     */
    public static void deletePersonFromDb(Person person, Context context) {
        init(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            if (db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(person.id) }) == 0) {
                Log.w(TAG, "No person available to be deleted with id " + person.id);
            } else {
                Log.v(TAG, "Deleting person");
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Load all the person we have in Db.
     * This method is expected to be called on App start only.
     */
    public static ArrayList<Person> loadAllAddresses(Context context) {

        ArrayList<Person> list = new ArrayList<Person>();

        init(context);
        // Read db and fill things back
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String[] cols = new String[11];
            cols[0] = "id";
            cols[1] = "name";
            cols[2] = "address";

            Cursor c = db.query(TABLE_NAME, cols, null, null, null,
                                null, null);
            while (c.move(1)) {
                Person  person = new Person();
                person.id = c.getLong(0);
                person.name = c.getString(1);
                person.address = c.getString(2);
                
                Log.v(TAG, "Loaded person from Db " + person.toString());

                // Add that to the list
                list.add(person);
            }
            c.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return list;
    }

}
