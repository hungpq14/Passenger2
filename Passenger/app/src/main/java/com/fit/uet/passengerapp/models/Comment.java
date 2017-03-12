package com.fit.uet.passengerapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Bien-kun on 10/03/2017.
 */
@IgnoreExtraProperties
public class Comment {
    public String userUid;
    public String content;
    public float star;
    public String timestamp;
    public String coachHostUid;
    public String name;


}
