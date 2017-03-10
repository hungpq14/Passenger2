package com.fit.uet.passengerapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bien-kun on 06/03/2017.
 */

@IgnoreExtraProperties
public class CoachHost {
    public String uid;
    public String description;
    public String name;
    public int numArrived;
    public int numLoved;
    public float star;
    public String website;
    public String logo;

}
