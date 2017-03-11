package com.fit.uet.passengerapp.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.ListenerEvent.CoachScheduleClickEvent;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.CoachManagerAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CoachManagerActivity extends BaseToolBarActivity {
    private String TAG = "CoachManagerActivity";
    private RecyclerView recyclerView;
    private ArrayList<CoachSchedule> coachSchedules;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coach_manager;
    }

    @Override
    protected String getToolbarText() {
        return "Quản lý chuyến xe";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDraw();
    }

    private void initDraw() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        coachSchedules = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(DB.SCHEDULE).child("cs0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CoachSchedule coachSchedule = dataSnapshot.getValue(CoachSchedule.class);
                coachSchedules.add(coachSchedule);
                CoachManagerAdapter adapter = new CoachManagerAdapter(coachSchedules);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickCoachScheduleEvent(CoachScheduleClickEvent event) {
        Log.d(TAG, "event clicked");
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
