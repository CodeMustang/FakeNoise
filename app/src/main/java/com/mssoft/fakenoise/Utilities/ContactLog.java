package com.mssoft.fakenoise.Utilities;

/**
 * Created by Marius on 9/24/2016.
 */

public class ContactLog {

    private String phone = "";
    private String name = "";
    private String time = "";
    private String noise = "";
    private long id = 0;


    public ContactLog(String name,String phone,String time,long id){
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.id = id;
    }

    public ContactLog(){

    }

    public void setId(long id){
        this.id = id;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNoise(String noise){
        this.noise = noise;
    }

    public void setTime(String time){
        this.time = time;
    }




    public long getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getTime(){
        return this.time;
    }

    public String getNoise(){
        return this.noise;
    }

}
