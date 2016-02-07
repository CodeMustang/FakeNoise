package com.mssoft.fakenoise.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marius on 9/28/2015.
 */
public class NoiseDatabase extends SQLiteOpenHelper {

    public static final String NOISE_DATABASE = "noises";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME ="name";
    public static final String COLUMN_NOISE = "noise";

    private static final String DATABASE_NAME = "noises.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + NOISE_DATABASE + "(" + COLUMN_ID +
            " integer primary key autoincrement, " + COLUMN_NAME + " text not null, " + COLUMN_NOISE + " media player);";

    public NoiseDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOISE_DATABASE);
        onCreate(db);
    }
}