package com.fit.uet.passengerapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bien-kun on 06/03/2017.
 */
@IgnoreExtraProperties
public class Coach {
    public static final String CHILD_COACH = "coach";

    public String uid;
    public String arriveFrom;
    public String arriveTo;
    public String coachHostUid;
    public int costPerTicket;
    public boolean hasShuttleBus;
    public boolean hasWater;
    public boolean hasWifi;
    public long kind;
    public String timeStart;


}
