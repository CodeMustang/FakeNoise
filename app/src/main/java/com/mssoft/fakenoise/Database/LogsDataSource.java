package com.mssoft.fakenoise.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mssoft.fakenoise.Utilities.ContactLog;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 9/24/2016.
 */

public class LogsDataSource {

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;
    private String[] allColumns = {LogsDatabase.COLUMN_ID,
            LogsDatabase.COLUMN_NAME,
            LogsDatabase.COLUMN_PHONE,
            LogsDatabase.COLUMN_NOISE,
            LogsDatabase.COLUMN_TIME};

    public LogsDataSource(Context context) {
        dbHelper = new LogsDatabase(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private ContactLog cursorToLog(Cursor cursor) {
        ContactLog log = new ContactLog();
        log.setId(cursor.getLong(0));
        log.setName(cursor.getString(1));
        log.setPhone(cursor.getString(2));
        log.setNoise(cursor.getString(3));
        log.setTime(cursor.getString(4));
        return log;
    }

    public ContactLog createLog(String name, String phone, String noise, String time) {

        ContentValues values = new ContentValues();
        values.put(LogsDatabase.COLUMN_NAME, name);
        values.put(LogsDatabase.COLUMN_PHONE, phone);
        values.put(LogsDatabase.COLUMN_NOISE,noise);
        values.put(LogsDatabase.COLUMN_TIME,time);

        long insertId = database.insert(LogsDatabase.TABLE_LOGS, null,
                values);
        Cursor cursor = database.query(LogsDatabase.TABLE_LOGS,
                allColumns, LogsDatabase.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ContactLog returnedLog = cursorToLog(cursor);
        cursor.close();
        return returnedLog;
    }

    private void deleteLog(ContactLog log) {
        long id = log.getId();
        database.delete(LogsDatabase.TABLE_LOGS, LogsDatabase.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllLogs() {
        ArrayList<ContactLog> logs = getAllLogs();

        for (int i = 0 ; i < logs.size(); i++) {
            deleteLog(logs.get(i));
        }
    }

    public ArrayList<ContactLog> getAllLogs() {
        ArrayList<ContactLog> list = new ArrayList<>();
        Cursor cursor = database.query(LogsDatabase.TABLE_LOGS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ContactLog log = cursorToLog(cursor);
            list.add(log);
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }
}
