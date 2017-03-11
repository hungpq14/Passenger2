package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fit.uet.passengerapp.Activity.fragments.FilterFragment;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.City;
import com.fit.uet.passengerapp.models.PickLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Bien-kun on 05/03/2017.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ArrayList<PickLocation> cities = new ArrayList<>();
//        cities.add(new PickLocation("0", "BX Giáp Bát", "Hanoi"));
//        cities.add(new PickLocation("1", "BX Mỹ Đình", "Hanoi"));
//        cities.add(new PickLocation("2", "BX Yên Nghĩa", "Hanoi"));
//        cities.add(new PickLocation("3", "BX Cầu Rào", "Hải Phòng"));
//        cities.add(new PickLocation("4", "VP 265 Trần Nguyên Hãn", "Hải Phòng"));
//        cities.add(new PickLocation("5", "BX Lào Cai", "Lào Cai"));
//        cities.add(new PickLocation("6", "BX Nam Định", "Nam Định"));
//        cities.add(new PickLocation("7", "Big C Nam Định", "Nam Định"));
//        cities.add(new PickLocation("8", "BX phía nam Nha Trang", "Nha Trang"));
//        cities.add(new PickLocation("9", "BX Đà Lạt", "Đà Lạt"));
//        cities.add(new PickLocation("10", "BX Thanh Hóa", "Thanh Hóa"));
//        cities.add(new PickLocation("11", "BX Hải Dương", "Hải Dương"));
//        cities.add(new PickLocation("12", "BX Bắc Giang", "Bắc Giang"));
//        cities.add(new PickLocation("13", "BX Hưng Yên", "Hưng Yên"));
//        cities.add(new PickLocation("14", "BX Đà Nẵng", "Đà Nẵng"));
//        cities.add(new PickLocation("15", "BX Lào Cai", "Lào Cai"));
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        for (PickLocation city : cities) {
//            reference.child("pick-location").child(city.getUid()).setValue(city);
//        }

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
    public void startCoachHostActivity(View view){
        Intent intent = new Intent(this,CoachHostActivity.class);
        intent.putExtra(CoachHostActivity.KEY_HOST_ID,"h1");
        startActivity(intent);
    }
}
