package com.fit.uet.passengerapp.ListenerEvent;

/**
 * Created by phamh_000 on 11/03/2017.
 */

public class CoachScheduleClickEvent {
    private int pos;

    public CoachScheduleClickEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
