package com.fit.uet.passengerapp.Activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.fit.uet.passengerapp.models.User;
import com.fit.uet.passengerapp.utils.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketBoxActivity extends BaseToolBarActivity implements View.OnClickListener {
    private static final int COLUMNS = 4;

    @BindView(R.id.btn_barcode_scan)
    FloatingActionButton btn_barcode_scan;

    @BindView(R.id.rv_seats)
    RecyclerView rv_seats;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.layout_main)
    View layout_main;

    private DatabaseReference databaseReference;

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

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
        hide();
        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        rv_seats.setLayoutManager(manager);

        databaseReference.child(Ticket.CHILD_TICKET).orderByChild("coach_schedule_id").equalTo(schedule.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ticket> tickets = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    tickets.add(snapshot.getValue(Ticket.class));
                }

                final List<AbstractItem> items = new ArrayList<>(20);
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

                final Hashtable<Integer, Ticket> seats = new Hashtable<>();
                for (Ticket ticket : tickets) {
                    for (int s : ticket.seats) {
                        seats.put(s, ticket);
                    }
                }

                adapter.setOnItemClickListener(new SeatsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        showSeatInfoDialog(position, seats.get(position));
                    }
                });

                show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSeatInfoDialog(int position, Ticket ticket) {
        if (ticket != null) {
            final Dialog dialog;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                dialog = new Dialog(this);
            }
            dialog.setContentView(R.layout.seat_info);
            dialog.setTitle("Ghế số " + position);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            final TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
            TextView tv_state = (TextView) dialog.findViewById(R.id.tv_state);
            final TextView tv_phone = (TextView) dialog.findViewById(R.id.tv_phone);
            TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
            final ImageView img_call = (ImageView) dialog.findViewById(R.id.img_call);
            final ImageView img_message = (ImageView) dialog.findViewById(R.id.img_message);

            databaseReference.child(DB.USER).child(ticket.user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final User user = dataSnapshot.getValue(User.class);

                    tv_name.setText(user.getName());
                    tv_phone.setText(user.getPhoneNum());

                    img_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Util.call(TicketBoxActivity.this, user.getPhoneNum());
                            dialog.dismiss();
                        }
                    });

                    img_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Util.sms(TicketBoxActivity.this, user.getPhoneNum());
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            if (ticket.checkout) {
                tv_state.setText("Đã xác nhận");
            } else {
                tv_state.setText("Chưa xác nhận");
            }
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Ghế chưa được đặt!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    public void update() {
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

    private void show() {
        layout_main.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void hide() {
        layout_main.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
}
