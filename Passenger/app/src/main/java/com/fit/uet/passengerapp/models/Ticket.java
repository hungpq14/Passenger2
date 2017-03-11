package com.fit.uet.passengerapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by phamtruong on 3/10/17.
 */

@IgnoreExtraProperties
public class Ticket {
    public static final String CHILD_TICKET = "ticket";

    public String coach_schedule_id;

    public List<Integer> seats;

    public String user_id;

    public boolean checkout;
}
