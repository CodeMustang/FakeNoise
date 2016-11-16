package com.mssoft.fakenoise.Utilities;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Marius on 10/1/2015.
 */
public class NoiseSounds {
    private long id;
    private String name;
    private boolean prepared = false;
    private MediaPlayer sound;
    private String path;


    public void setId(long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }

    public void setSound(MediaPlayer sound){
        this.sound = sound;
    }

    public long getId(){
        return id;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getName(){
        return name;
    }
    public String getPath(){
        return path;
    }
    public MediaPlayer getSound(){
        return sound;
    }

}
