package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.ListenerEvent.EventPickLocation;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.CityAdapter;
import com.fit.uet.passengerapp.models.City;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ListCityActivity extends BaseToolBarActivity {
    private String TAG = "ListCityActivity";
    private RecyclerView recyclerView;
    private ArrayList<City> cities;
    private int positionEventPicked = 99;
    private int REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDraw();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void initDraw() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        cities = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("city").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    cities.add(snapshot.getValue(City.class));
                }
                CityAdapter cityAdapter = new CityAdapter(cities);
                recyclerView.setAdapter(cityAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_city;
    }

    @Override
    protected String getToolbarText() {
        return "Chọn tên thành phố";
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPickCityEvent(EventPickLocation event) {
        positionEventPicked = event.getPosition();
        finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("pos", positionEventPicked);
        setResult(0, intent);
        super.finish();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
