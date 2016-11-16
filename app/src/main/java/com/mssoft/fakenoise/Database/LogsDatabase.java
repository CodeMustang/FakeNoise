package com.mssoft.fakenoise.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marius on 9/24/2016.
 */

public class LogsDatabase extends SQLiteOpenHelper {

    public static final String TABLE_LOGS = "logs";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_NOISE = "noise";
    public static final String COLUMN_TIME = "time";


    private static final String DATABASE_NAME = "logs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_DATABASE = "create table "
            + TABLE_LOGS + "(" + COLUMN_ID +
            " integer primary key autoincrement, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_PHONE + " text not null, " +
            COLUMN_NOISE + " text not null, " +
            COLUMN_TIME + " text not null)";


    public LogsDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        onCreate(db);
    }
}
