package com.fit.uet.passengerapp.models;

/**
 * Created by phamh_000 on 11/03/2017.
 */

public class PickLocation {
    private String uid;
    private String name;
    private String cityName;

    public PickLocation(){
    }

    public PickLocation(String uid, String name, String cityName) {
        this.uid = uid;
        this.name = name;
        this.cityName = cityName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
