package com.fit.uet.passengerapp.models;

/**
 * Created by phamh_000 on 11/03/2017.
 */

public class City {
    private String uid;
    private String name;

    public City() {
    }

    public City(String uid, String name) {
        this.uid = uid;
        this.name = name;
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
}
