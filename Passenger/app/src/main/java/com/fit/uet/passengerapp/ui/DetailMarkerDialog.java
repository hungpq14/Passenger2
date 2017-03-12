package com.fit.uet.passengerapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.activities.MessageListActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.CoachRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by phamh_000 on 12/03/2017.
 */

public class DetailMarkerDialog extends Dialog {
    private Activity activity;
    private CoachRequest coachRequest;

    public DetailMarkerDialog(@NonNull Activity activity, CoachRequest coachRequest) {
        super(activity);
        this.activity = activity;
        this.coachRequest = coachRequest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_marker);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOwnerActivity(activity);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = width / 4 * 3;
        getWindow().setAttributes(lp);

        initDraw();


        ((Button) findViewById(R.id.btn_message)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = coachRequest.user_id;


                Intent intent = new Intent(activity, MessageListActivity.class);
                intent.putExtra(MessageListActivity.KEY_DESTINATION,destination);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public void initDraw() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String userid = coachRequest.user_id;
        myRef.child("users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                ((TextView) findViewById(R.id.txt_user_name)).setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((TextView) findViewById(R.id.txt_ticket_count)).setText(coachRequest.numTicket + "");
        ((TextView) findViewById(R.id.txt_min_date)).setText(coachRequest.timeMinToStart);
        ((TextView) findViewById(R.id.txt_max_date)).setText(coachRequest.timeMaxToStart);
        ((TextView) findViewById(R.id.txt_from)).setText(coachRequest.arriveFrom);
        ((TextView) findViewById(R.id.txt_to)).setText(coachRequest.arriveTo);

    }

}
