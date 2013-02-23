
package com.beanie.map101;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

public class DBHelper {
    private SQLiteDatabase sqlDB;

    private DatabaseHelper dbHelper;

    public DBHelper(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public DBHelper open() throws SQLException {
        this.sqlDB = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long addNewLocation(MapLocation location) {
        ContentValues values = new ContentValues();
        values.put(IDBValues.COL_LAT, location.getLatitude());
        values.put(IDBValues.COL_LONG, location.getLongitude());
        values.put(IDBValues.COL_NAME, location.getName());
        values.put(IDBValues.COL_DESCRIPTION, location.getDescription());

        long _id = sqlDB.insert(IDBValues.TABLE_LOCATIONS, null, values);
        return _id;
    }

    public ArrayList<MapLocation> getSavedLocations(VisibleRegion visibleRegion) {
        LatLngBounds bounds = visibleRegion.latLngBounds;
        ArrayList<MapLocation> locations = new ArrayList<MapLocation>();
        Cursor cursor = sqlDB.query(IDBValues.TABLE_LOCATIONS, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            MapLocation location = new MapLocation();
            location.set_id(cursor.getLong(cursor.getColumnIndex(IDBValues.COL_ID)));
            location.setLatitude(Double.parseDouble(cursor.getString(cursor
                    .getColumnIndex(IDBValues.COL_LAT))));
            location.setLongitude(Double.parseDouble(cursor.getString(cursor
                    .getColumnIndex(IDBValues.COL_LONG))));
            location.setName(cursor.getString(cursor.getColumnIndex(IDBValues.COL_NAME)));
            location.setDescription(cursor.getString(cursor
                    .getColumnIndex(IDBValues.COL_DESCRIPTION)));
            /**
             * VisibleRegion's contains method deosn't seem to be working.
             */
            // if (bounds.contains(location.getLatLng()))
            locations.add(location);

        }
        return locations;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, IDBConstants.DATABASE_NAME, null, IDBConstants.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IDBValues.CREATE_TABLE_LOCATIONS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public MapLocation getLocationByName(String title) {
        MapLocation location = null;
        String selection = IDBValues.COL_NAME + " = ?";
        String[] selectionArgs = {
            title
        };
        Cursor cursor = sqlDB.query(IDBValues.TABLE_LOCATIONS, null, selection, selectionArgs,
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            location = new MapLocation();
            location.set_id(cursor.getLong(cursor.getColumnIndex(IDBValues.COL_ID)));
            location.setLatitude(Double.parseDouble(cursor.getString(cursor
                    .getColumnIndex(IDBValues.COL_LAT))));
            location.setLongitude(Double.parseDouble(cursor.getString(cursor
                    .getColumnIndex(IDBValues.COL_LONG))));
            location.setName(cursor.getString(cursor.getColumnIndex(IDBValues.COL_NAME)));
            location.setDescription(cursor.getString(cursor
                    .getColumnIndex(IDBValues.COL_DESCRIPTION)));
        }
        cursor.close();
        return location;
    }

    public boolean doesNameExist(String name) {
        boolean exists = false;
        String selection = IDBValues.COL_NAME + " = ?";
        String[] selectionArgs = {
            name
        };
        Cursor cursor = sqlDB.query(IDBValues.TABLE_LOCATIONS, null, selection, selectionArgs,
                null, null, null);
        if (cursor.getCount() > 0) {
            exists = true;
        }
        return exists;
    }
}
