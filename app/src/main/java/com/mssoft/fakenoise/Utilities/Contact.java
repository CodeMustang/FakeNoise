package com.mssoft.fakenoise.Utilities;

/**
 * Created by Marius on 9/16/2015.
 */
public class Contact {
    private String name;
    private String phone;
    private String path;
    private String noise;
    private long id;

    public void setPath(String path){this.path = path;}
    public void setName (String name){
        this.name = name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setNoise(String noise){this.noise = noise;};

    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public String getPath(){return  path;}
    public String getNoise(){return noise;}
    public long getId(){
        return id;
    }
}
