package com.fit.uet.passengerapp.Activity.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.fit.uet.passengerapp.models.Ticket;
import com.fit.uet.passengerapp.utils.DialogUtils;
import com.google.firebase.auth.FirebaseAuth;
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
    private static final int COLUMNS = 4;

    @BindView(R.id.rv_seats)
    RecyclerView rv_seats;

    @BindView(R.id.tv_seats)
    TextView tv_seats;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_next)
    TextView tv_next;

    private DatabaseReference databaseReference;
    private DatabaseReference scheduleDatabaseReference;
    private DatabaseReference ticketDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        scheduleDatabaseReference = databaseReference.child(CoachSchedule.CHILD_COACH_SCHEDULE);
        ticketDatabaseReference = databaseReference.child(Ticket.CHILD_TICKET);

        scheduleDatabaseReference.child(getIntent().getStringExtra(Intent.EXTRA_TEXT)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CoachSchedule coachSchedule = dataSnapshot.getValue(CoachSchedule.class);

                initSeatView(coachSchedule);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initSeatView(final CoachSchedule schedule) {
        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        rv_seats.setLayoutManager(manager);

        List<AbstractItem> items = new ArrayList<>();
        for (int i = 0; i < schedule.seatState.length(); i++) {
            if (schedule.seatState.charAt(i) == '0') {
                items.add(new CenterItem(schedule.seatState.charAt(i) + ""));
            } else if (schedule.seatState.charAt(i) == '3') {
                items.add(new OrderedItem(schedule.seatState.charAt(i) + ""));
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
                tv_price.setText(count * schedule.costPerTicket + "$");
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelectedItems().size() != 0) {
                    DialogUtils.showConfirmDialog(SeatBookingActivity.this, "Confirm Your In App Purchase", "Do you want to buy tickets?", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            List<Integer> selectedItems = adapter.getSelectedItems();

                            Ticket ticket = new Ticket();
                            ticket.checkout = false;
                            ticket.coach_schedule_id = schedule.uid;
                            ticket.seats = selectedItems;
                            ticket.user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            ticketDatabaseReference.push().setValue(ticket);

                            for (Integer seat : selectedItems) {
                                StringBuilder sb = new StringBuilder(schedule.seatState);
                                sb.setCharAt(seat, '3');
                                schedule.seatState = sb.toString();
                            }

                            scheduleDatabaseReference.child(getIntent().getStringExtra(Intent.EXTRA_TEXT)).child("seatState").setValue(schedule.seatState);

                            startActivity(new Intent(SeatBookingActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }
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
