package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.SeatsAdapter;
import com.fit.uet.passengerapp.models.AbstractItem;
import com.fit.uet.passengerapp.models.CenterItem;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.fit.uet.passengerapp.models.EdgeItem;
import com.fit.uet.passengerapp.models.EmptyItem;
import com.fit.uet.passengerapp.models.OrderedItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeatBookingActivity extends BaseToolBarActivity {
    private static final int COLUMNS = 5;

    @BindView(R.id.rv_seats)
    RecyclerView rv_seats;

    @BindView(R.id.tv_seats)
    TextView tv_seats;

    @BindView(R.id.tv_price)
    TextView tv_price;

    private DatabaseReference databaseReference;
    private DatabaseReference scheduleDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        scheduleDatabaseReference = databaseReference.child(CoachSchedule.CHILD_COACH_SCHEDULE);
        scheduleDatabaseReference.child(getIntent().getStringExtra(Intent.EXTRA_TEXT)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CoachSchedule coachSchedule = dataSnapshot.getValue(CoachSchedule.class);

                initSeatView(coachSchedule.seatState + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initSeatView(String seats) {
        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        rv_seats.setLayoutManager(manager);

        List<AbstractItem> items = new ArrayList<>();
        for (int i = 0; i < seats.length(); i++) {
            if (seats.charAt(i) == '0') {
                items.add(new CenterItem(seats.charAt(i) + ""));
            } else if (seats.charAt(i) == '1') {
                items.add(new EdgeItem(seats.charAt(i) + ""));
            } else if (seats.charAt(i) == '2') {
                items.add(new EmptyItem(seats.charAt(i) + ""));
            } else if (seats.charAt(i) == '3') {
                items.add(new OrderedItem(seats.charAt(i) + ""));
            }
        }

        final SeatsAdapter adapter = new SeatsAdapter(this, items);
        rv_seats.setAdapter(adapter);

        adapter.setmOnSeatSelected(new SeatsAdapter.OnSeatSelected() {
            @Override
            public void onSeatSelected(int count) {
                if (count != 0) {
                    tv_seats.setText(adapter.getSelectedItems().toString());
                } else {
                    tv_seats.setText("");
                }
                tv_price.setText(count * 5 + "$");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seat_booking;
    }

    @Override
    protected String getToolbarText() {
        return "Booking";
    }
}
