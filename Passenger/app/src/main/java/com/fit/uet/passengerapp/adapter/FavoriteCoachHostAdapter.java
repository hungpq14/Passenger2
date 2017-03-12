package com.fit.uet.passengerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;
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
import java.util.List;

/**
 * Created by Bien-kun on 11/03/2017.
 */

public class FavoriteCoachHostAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> implements ValueEventListener, ChildEventListener {
    private List<CoachSchedule> mSchedules;
    private Query mKeyQuery;
    private DatabaseReference mRef;
    private TextView mEmpty;

    public FavoriteCoachHostAdapter(DatabaseReference ref, Query keyQuery) {
        mSchedules = new ArrayList<>();
        mKeyQuery = keyQuery;
        mRef = ref;
        mKeyQuery.addChildEventListener(this);

    }

    public void cleanup() {

    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ScheduleHolder holder, int position) {
        CoachSchedule schedule = mSchedules.get(position);
        holder.bind(schedule, schedule.coach, schedule.coachHost, null);
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }

    @Override
    public ScheduleAdapter.ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_schedule_item, parent, false);
        return new ScheduleAdapter.ScheduleHolder(view);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        validate();
    }

    private void validate() {
        if (mEmpty == null) {
            return;
        }
        if (mSchedules.size() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        mRef.child(DB.COACH_HOST).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final CoachHost coachHost = dataSnapshot.getValue(CoachHost.class);
                mRef.child(DB.COACH).orderByChild("coachHostUid").equalTo(coachHost.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                            for (DataSnapshot childSnapshot :
                                    dataSnapshot.getChildren()) {
                                final Coach coach = childSnapshot.getValue(Coach.class);
                                mRef.child(DB.SCHEDULE).orderByChild("coachUid").equalTo(coach.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                                            for (DataSnapshot childSnapshot :
                                                    dataSnapshot.getChildren()) {
                                                CoachSchedule schedule = childSnapshot.getValue(CoachSchedule.class);
                                                schedule.coachHost = coachHost;
                                                schedule.coach = coach;
                                                addItem(schedule);
                                                validate();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addItem(CoachSchedule schedule) {
        mSchedules.add(schedule);
        notifyItemInserted(mSchedules.size() - 1);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void setupWithEmptyView(TextView textView) {
        this.mEmpty = textView;
    }
}
