
package com.beanie.map101;

public interface IDBValues {
    String TABLE_LOCATIONS = "locations";

    String COL_ID = "_id";

    String COL_LAT = "latitude";

    String COL_LONG = "longitude";

    String COL_NAME = "loc_name";

    String COL_DESCRIPTION = "loc_description";

    String CREATE_TABLE_LOCATIONS = "create table " + TABLE_LOCATIONS + "(" + COL_ID
            + " integer primary key autoincrement," + COL_LAT + " text, " + COL_LONG + " text,"
            + COL_NAME + " text," + COL_DESCRIPTION + " text);";
}
