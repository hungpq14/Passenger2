package com.fit.uet.passengerapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bien-kun on 05/03/2017.
 */

@IgnoreExtraProperties
public class CoachSchedule {
    public static final String CHILD_COACH_SCHEDULE = "coach-schedule";

    public String uid;
    public String coachUid;
    public String departureTime;
    public int seatAvailable;
    public boolean hasShuttleBus;
    public String seatState;

    public String arriveFrom, arriveTo, pickFrom, pickTo;
    public long costPerTicket;

}
