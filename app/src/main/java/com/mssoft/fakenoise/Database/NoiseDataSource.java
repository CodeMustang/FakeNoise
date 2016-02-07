package com.mssoft.fakenoise.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.CursorAdapter;

import com.mssoft.fakenoise.NoiseSounds;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 10/1/2015.
 */
public class NoiseDataSource {
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;
    private Context mContext;
    private String[] columns = {NoiseDatabase.COLUMN_ID,NoiseDatabase.COLUMN_NAME,NoiseDatabase.COLUMN_NOISE};

    public NoiseDataSource (Context context){
        dbHelper = new NoiseDatabase(context);
        mContext = context;
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    private NoiseSounds cursorToNoise(Cursor cursor) throws IOException {
        NoiseSounds noise = new NoiseSounds();
        noise.setId(cursor.getLong(0));
        noise.setName(cursor.getString(1));
        String path = cursor.getString(2);
        MediaPlayer sound = new MediaPlayer();
        sound.setDataSource(mContext, Uri.parse(path));
        noise.setSound(sound);

        return noise;
    }

    public NoiseSounds createNoise(String name , String path) throws IOException {
        ContentValues values = new ContentValues();
        values.put(NoiseDatabase.COLUMN_NAME, name);
        values.put(NoiseDatabase.COLUMN_NOISE, path);
        long insertId = database.insert(NoiseDatabase.NOISE_DATABASE, null,
                values);
        Cursor cursor = database.query(NoiseDatabase.NOISE_DATABASE,
                columns, NoiseDatabase.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        NoiseSounds returnedNoise = cursorToNoise(cursor);
        cursor.close();

        return returnedNoise;
    }

    public void deleteNoise(NoiseSounds noise){
        long id = noise.getId();
        database.delete(NoiseDatabase.NOISE_DATABASE, NoiseDatabase.COLUMN_ID + " = " + id, null);
    }

    public ArrayList<NoiseSounds> getAllNoises() throws IOException {
        ArrayList<NoiseSounds> list = new ArrayList<>();
        Cursor cursorList = database.query(NoiseDatabase.NOISE_DATABASE,columns,null,null,null,null,null);
        cursorList.moveToFirst();
        while(!cursorList.isAfterLast()){
            NoiseSounds sounds = cursorToNoise(cursorList);
            list.add(sounds);
            cursorList.moveToNext();
        }
        cursorList.close();

        return list;
    }
}