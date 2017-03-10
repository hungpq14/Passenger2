package com.fit.uet.passengerapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bien-kun on 06/03/2017.
 */
@IgnoreExtraProperties
public class Coach {
    public static final String CHILD_COACH = "coach";

    public String uid;

    public String coachHostUid;

    public boolean hasWater;
    public boolean hasWifi;
    public long kind;
    public String timeStart;


}
