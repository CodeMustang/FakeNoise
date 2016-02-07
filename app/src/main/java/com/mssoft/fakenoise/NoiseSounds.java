package com.mssoft.fakenoise;

import android.media.MediaPlayer;

/**
 * Created by Marius on 10/1/2015.
 */
public class NoiseSounds {
    private long id;
    private String name;
    private boolean prepared = false;
    private MediaPlayer sound = new MediaPlayer();

    public void setId(long id){
        this.id = id;
    }
    public void setPrepared(boolean prepared){
        this.prepared = prepared;
    }
    public boolean getPrepared(){
        return prepared;
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

    public String getName(){
        return name;
    }
    public MediaPlayer getSound(){
        return sound;
    }
}
