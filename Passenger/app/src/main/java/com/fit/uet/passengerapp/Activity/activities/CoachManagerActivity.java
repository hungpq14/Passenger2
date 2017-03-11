package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.CoachManagerAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.fit.uet.passengerapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoachManagerActivity extends BaseToolBarActivity {
    private String TAG = "CoachManagerActivity";
    private RecyclerView recyclerView;
    private ArrayList<CoachSchedule> coachSchedules;
    private ImageView imgScanning;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
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
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initDraw();
    }

    private void initDraw() {
        imgScanning = (ImageView) findViewById(R.id.img_scan);
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(300);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imgScanning.startAnimation(rotateAnimation);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference.child(DB.USER).child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                databaseReference.child(DB.SCHEDULE).orderByChild("coachUid").equalTo(user.getCoachUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<CoachSchedule> coachSchedules = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            coachSchedules.add(snapshot.getValue(CoachSchedule.class));
                        }

                        CoachManagerAdapter adapter = new CoachManagerAdapter(coachSchedules);
                        recyclerView.setAdapter(adapter);
                        imgScanning.clearAnimation();
                        imgScanning.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter.setOnItemClickListener(new CoachManagerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(CoachManagerActivity.this, TicketBoxActivity.class);
                                intent.putExtra(Intent.EXTRA_TEXT, coachSchedules.get(position).uid);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Cancel", databaseError.getMessage());
            }
        });
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onClickCoachScheduleEvent(CoachScheduleClickEvent event) {
//        //todo new event here
//        Log.d(TAG, "Clicked item");
//        Intent intent = new Intent(CoachManagerActivity.this, ShuttleBusSuggestionActivity.class);
//        startActivity(intent);
//    }
}
