package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.Activity.fragments.ScannerFragment;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.SeatsAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.AbstractItem;
import com.fit.uet.passengerapp.models.CenterItem;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.fit.uet.passengerapp.models.OrderedItem;
import com.fit.uet.passengerapp.models.Ticket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketBoxActivity extends BaseToolBarActivity implements View.OnClickListener{
    private static final int COLUMNS = 4;

    @BindView(R.id.btn_barcode_scan)
    FloatingActionButton btn_barcode_scan;

    @BindView(R.id.rv_seats)
    RecyclerView rv_seats;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        btn_barcode_scan.setOnClickListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    btn_barcode_scan.setVisibility(View.GONE);
                } else {
                    btn_barcode_scan.setVisibility(View.VISIBLE);
                }
            }
        });

        databaseReference.child(DB.SCHEDULE).child(getIntent().getStringExtra(Intent.EXTRA_TEXT)).addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ticket_box;
    }

    @Override
    protected String getToolbarText() {
        return "Chuyến xe";
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_barcode_scan:
                scan();
        }
    }

    private void scan() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ScannerFragment.newInstance()).addToBackStack(null).commit();
    }

    private void initSeatView(final CoachSchedule schedule) {
        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        rv_seats.setLayoutManager(manager);

        databaseReference.child(Ticket.CHILD_TICKET).orderByChild("coach_schedule_id").equalTo(schedule.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ticket> tickets = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    tickets.add(snapshot.getValue(Ticket.class));
                }

                List<AbstractItem> items = new ArrayList<>(20);
                for (int i = 0; i < 20; i++) {
                    items.add(new CenterItem(String.valueOf(i)));
                }

                for (Ticket ticket : tickets) {
                    if (ticket.checkout) {
                        for (Integer i : ticket.seats) {
                            items.set(i, new OrderedItem(String.valueOf(i)));
                        }
                    }
                }

                final SeatsAdapter adapter = new SeatsAdapter(TicketBoxActivity.this, items);
                rv_seats.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
