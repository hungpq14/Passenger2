package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fit.uet.passengerapp.Activity.fragments.FilterFragment;
import com.fit.uet.passengerapp.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Bien-kun on 05/03/2017.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            startActivity(new Intent(this, MainUIActivity.class));
        }

        findViewById(R.id.btn_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TicketActivity.class));
            }
        });
    }

    public void startLogin(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }

    public void startFilter(View view) {
        startActivity(new Intent(this, FilterFragment.class));
    }

    public void startSchedule(View view) {
        startActivity(new Intent(this, ActivityScheduleList.class));
    }

    public void startConversation(View view) {
        startActivity(new Intent(this, ConversationActivity.class));
    }
}
