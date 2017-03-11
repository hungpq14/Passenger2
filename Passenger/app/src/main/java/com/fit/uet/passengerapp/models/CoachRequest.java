package com.fit.uet.passengerapp.models;

/**
 * Created by phamh_000 on 12/03/2017.
 */

public class CoachRequest {

    public String arriveTo, arriveFrom;
    public int numTicket;
    public String timeMaxToStart;
    public String timeMinToStart;
    public String user_id;
    public String uid;

    public CoachRequest() {
    }

    public CoachRequest(int num) {
        this.numTicket = num;
    }

    public CoachRequest(String arriveTo, String arriveFrom, int numTicket, String timeMaxToStart, String timeMinToStart, String user_id) {
        this.arriveTo = arriveTo;
        this.arriveFrom = arriveFrom;
        this.numTicket = numTicket;
        this.timeMaxToStart = timeMaxToStart;
        this.timeMinToStart = timeMinToStart;
        this.user_id = user_id;
    }
}
