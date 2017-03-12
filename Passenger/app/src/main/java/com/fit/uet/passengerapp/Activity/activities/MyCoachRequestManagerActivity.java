package com.fit.uet.passengerapp.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.ListenerEvent.EventPickLocation;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.CityAdapter;
import com.fit.uet.passengerapp.adapter.MyCoachRequestAdapter;
import com.fit.uet.passengerapp.models.City;
import com.fit.uet.passengerapp.models.CoachRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MyCoachRequestManagerActivity extends BaseToolBarActivity {
    ImageView imgScan;
    RecyclerView recyclerView;
    ArrayList<CoachRequest> coachRequests;
    MyCoachRequestAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_coach_request_manager;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected String getToolbarText() {
        return "Quản lý yêu cầu đã gửi";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDraw();
    }

    private void initDraw() {
        imgScan = (ImageView) findViewById(R.id.img_scan);
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(300);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imgScan.startAnimation(rotateAnimation);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        coachRequests = new ArrayList<>();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("coach-request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(CoachRequest.class).user_id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        coachRequests.add(snapshot.getValue(CoachRequest.class));
                }
                adapter = new MyCoachRequestAdapter(coachRequests);
                recyclerView.setAdapter(adapter);
                imgScan.clearAnimation();
                imgScan.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCancelEvent(final EventPickLocation event) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String key = coachRequests.get(event.getPosition()).uid;
        reference.child("coach-request").child(key).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                coachRequests.remove(event.getPosition());
                adapter.notifyItemRemoved(event.getPosition());
            }
        });
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
