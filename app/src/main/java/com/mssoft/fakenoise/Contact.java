package com.mssoft.fakenoise;

/**
 * Created by Marius on 9/16/2015.
 */
public class Contact {
    private String name;
    private String phone;
    private long id;

    public void setName (String name){
        this.name = name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public long getId(){
        return id;
    }
}
