package com.fit.uet.passengerapp.adapter;

import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.Coach;
import com.fit.uet.passengerapp.models.CoachHost;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Bien-kun on 08/03/2017.
 */

class ScheduleArray implements ChildEventListener, ValueEventListener {
    private DatabaseReference mRef;
    private Query mQuery;
    private ChangeEventListener mListener;
    List<CoachSchedule> mSnapshots = new ArrayList<>();
    HashMap<String, Coach> mCoachMap;
    HashMap<String, CoachHost> mCoachHostMap;
    private String mFrom, mTo;
    private Boolean hasShuttleBus;

    public ScheduleArray(DatabaseReference ref, Query query, String from, String to, Boolean hasShuttleBus) {
        mRef = ref;
        mQuery = query;
        mQuery.addChildEventListener(this);
        mQuery.addValueEventListener(this);
        mCoachMap = new HashMap<>();
        mCoachHostMap = new HashMap<>();
        mFrom = from;
        mTo = to;
        this.hasShuttleBus = hasShuttleBus;
    }


    public void requery(Query query) {
        mQuery = query;
        mQuery.addChildEventListener(this);
        mQuery.addValueEventListener(this);
    }

    public void cleanup() {
        mQuery.removeEventListener((ValueEventListener) this);
        mQuery.removeEventListener((ChildEventListener) this);
        mSnapshots.clear();
//        Iterator<Coach> ir = mCoachMap.values().iterator();
//        while (ir.hasNext()) {
//            Coach coach = ir.next();
//            mRef.child(DB.COACH).child(coach.uid).removeEventListener(mCoachListener);
//        }
//
//        Iterator<CoachHost> iter = mCoachHostMap.values().iterator();
//        while (iter.hasNext()) {
//            CoachHost coachHost = iter.next();
//            mRef.child(DB.COACH_HOST).child(coachHost.uid).removeEventListener(mCoachListener);
//        }

    }

    public int getCount() {
        return mSnapshots.size();
    }

    public CoachSchedule getItem(int index) {
        return mSnapshots.get(index);
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (CoachSchedule snapshot : mSnapshots) {
            if (snapshot.uid.equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        final CoachSchedule coachSchedule = snapshot.getValue(CoachSchedule.class);
        if (!coachSchedule.arriveFrom.equals(mFrom)
                || !coachSchedule.arriveTo.equals(mTo)
                ) {
            return;
        }

//        int index = 0;
//        if (previousChildKey != null) {
//            index = getIndexForKey(previousChildKey) + 1;
//        }

        final int position = mSnapshots.size() - 1;
        mSnapshots.add(coachSchedule);
        if (!mCoachMap.containsKey(coachSchedule.coachUid)) {
            mRef.child(DB.COACH).child(coachSchedule.coachUid).addValueEventListener(mCoachListener);
        }
        notifyChangedListeners(ChangeEventListener.EventType.ADDED, position);
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
//        int index = getIndexForKey(snapshot.getKey());
//        //TODO store or query again to get coach data
//        mSnapshots.set(index, snapshot.getValue(CoachSchedule.class));
//        notifyChangedListeners(ChangeEventListener.EventType.CHANGED, index);
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
//        int index = getIndexForKey(snapshot.getKey());
//        mSnapshots.remove(index);
//        notifyChangedListeners(ChangeEventListener.EventType.REMOVED, index);
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
//        //TODO store or query again to get coach data
//        int oldIndex = getIndexForKey(snapshot.getKey());
//        CoachSchedule coachSchedule = mSnapshots.remove(oldIndex);
//        int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
//        mSnapshots.add(newIndex, coachSchedule);
//        notifyChangedListeners(ChangeEventListener.EventType.MOVED, newIndex, oldIndex);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mListener.onDataChanged();
    }

    @Override
    public void onCancelled(DatabaseError error) {
        notifyCancelledListeners(error);
    }

    public void setOnChangedListener(ChangeEventListener listener) {
        mListener = listener;
    }

    protected void notifyChangedListeners(ChangeEventListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(ChangeEventListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChildChanged(type, index, oldIndex);
        }
    }

    protected void notifyCancelledListeners(DatabaseError error) {
        if (mListener != null) {
            mListener.onCancelled(error);
        }
    }

    private void onCoachChange(Coach coach) {

        for (int i = 0; i < mSnapshots.size(); i++) {
            if (mSnapshots.get(i).coachUid.equals(coach.uid)) {
                notifyChangedListeners(ChangeEventListener.EventType.CHANGED, i);
            }
        }
    }

    private void onCoachHostChange(CoachHost coachHost) {

        Iterator<Coach> ir = mCoachMap.values().iterator();
        while (ir.hasNext()) {
            Coach coach = ir.next();
            if (coachHost.uid.equals(coach.coachHostUid)) {
                onCoachChange(coach);
            }
        }
    }


    private ValueEventListener mCoachListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Coach coach = dataSnapshot.getValue(Coach.class);
            if (coach == null) {
                return;
            }
            mCoachMap.put(coach.uid, coach);
            onCoachChange(coach);
            if (!mCoachHostMap.containsKey(coach.coachHostUid)) {
                mRef.child(DB.COACH_HOST + "/" + coach.coachHostUid).addValueEventListener(mCoachHostListener);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            notifyCancelledListeners(databaseError);
        }
    };

    private ValueEventListener mCoachHostListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            CoachHost coachHost = dataSnapshot.getValue(CoachHost.class);
            if (coachHost == null) {
                return;
            }
            mCoachHostMap.put(coachHost.uid, coachHost);
            onCoachHostChange(coachHost);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            notifyCancelledListeners(databaseError);
        }
    };

}
