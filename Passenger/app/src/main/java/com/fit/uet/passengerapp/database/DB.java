package com.fit.uet.passengerapp.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Bien-kun on 06/03/2017.
 */

public class DB {
    public static final String SCHEDULE = "coach-schedule";
    public static final String COACH_HOST="coach-host";
    public static final String COACH = "coach";


    public static final String CHAT = "chat";
    public static final String USER = "users";
    public static final String MESSAGES = "messages";
    public static final String COMMENTS = "comments";
    public static final String COACH_HOST_FAV = "coach-host-fav";

    public static Query getLastMessageForUserRef(DatabaseReference mRef,String self,String destination) {
        return mRef.child(DB.CHAT).child(self).child(destination).child(MESSAGES).limitToLast(1);
    }

    public static Query getMessageForUserRef(DatabaseReference mRef,String self,String destination) {
        return mRef.child(DB.CHAT).child(self).child(destination).child(MESSAGES);
    }
}
