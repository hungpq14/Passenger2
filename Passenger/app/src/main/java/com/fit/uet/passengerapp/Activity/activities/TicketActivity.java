package com.fit.uet.passengerapp.Activity.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.Coach;
import com.fit.uet.passengerapp.models.Ticket;
import com.fit.uet.passengerapp.utils.BarcodeUtils;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketActivity extends AppCompatActivity {
    @BindView(R.id.tv_arrive_from)
    TextView tv_arrive_from;

    @BindView(R.id.tv_arrive_to)
    TextView tv_arrive_to;

    @BindView(R.id.img_barcode)
    ImageView img_barcode;

    @BindView(R.id.tv_code)
    TextView tv_code;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_time)
    TextView tv_time;

    private DatabaseReference databaseReference;
    private DatabaseReference ticketDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ticketDatabaseReference = databaseReference.child(Ticket.CHILD_TICKET);
        Query ticketQuery = ticketDatabaseReference.orderByChild("user_id").equalTo("d7iKFdtKZAZflYr9y1hDJ566UXq2");
        ticketQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Ticket ticket = singleSnapshot.getValue(Ticket.class);
                    setBarcode(singleSnapshot.getKey());
                    databaseReference.child(Coach.CHILD_COACH).child(ticket.coach_schedule_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Coach coach = dataSnapshot.getValue(Coach.class);
                            tv_arrive_from.setText(coach.arriveFrom);
                            tv_arrive_to.setText(coach.arriveTo);
                            tv_price.setText(coach.costPerTicket + "$");

                            long ms = DateTimeUtils.getMillisFromString(coach.timeStart);
                            tv_date.setText(DateTimeUtils.dateStringFormat(ms));
                            tv_time.setText(DateTimeUtils.getTimeFromMs(ms));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getDetails());
            }
        });

    }

    private void setBarcode(String code) {
        tv_code.setText(code);

        Bitmap barcodeBitmap;
        try {
            barcodeBitmap = BarcodeUtils.encodeAsBitmap(code, BarcodeFormat.CODE_128, 400, 100);
            img_barcode.setImageBitmap(barcodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
