package com.fit.uet.passengerapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bien-kun on 05/03/2017.
 */

@IgnoreExtraProperties
public class CoachSchedule {
    public String uid;
    public String coachUid;
    public String departureTime;
    public int seatAvailable;
    public String arriveFrom;
    public String arriveTo;
    public int costPerTicket;
    public boolean hasShuttleBus;

}
