package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.Activity.fragments.FilterFragment;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.ScheduleAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

/**
 * Created by Bien-kun on 05/03/2017.
 */

public class ActivityScheduleList extends BaseToolBarActivity {
    private RecyclerView mList;
    private ScheduleAdapter mAdapter;
    private DatabaseReference mRef;

    private String mFrom, mTo;
    private long mTime;
    private Bundle serviceBundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = (RecyclerView) findViewById(R.id.recycler);
        mRef = FirebaseDatabase.getInstance().getReference();
        initCalendar();
        initViews();

        mFrom = getIntent().getStringExtra(FilterFragment.LOCATION_FROM);
        mTo = getIntent().getStringExtra(FilterFragment.LOCATION_TO);
        mTime = getIntent().getLongExtra(FilterFragment.DATE, 0);
        serviceBundle = getIntent().getBundleExtra(FilterFragment.SERVICE);
    }

    private void initCalendar() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.tab_calendar)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Log.d("ActivitySchedule", "Date changed: " + date.getTime());
                mAdapter.requery(getQuery(date));
            }
        });
    }

    private Query getQuery(Date date) {

        String dateFormat = (new SimpleDateFormat("yyy/MM/dd")).format(date.getTime());
        Query query = mRef.child(DB.SCHEDULE).orderByChild("departureTime").startAt(dateFormat)
                .orderByChild("arriveFrom").equalTo(mFrom)
                .orderByChild("arriveTo").equalTo(mTo);
        return query;
    }

    private void initViews() {
        mAdapter = new ScheduleAdapter(mRef, getQuery(new Date()));
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAdapter);

        mAdapter.setOnCLickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CoachSchedule coachSchedule) {
                Intent intent = new Intent(ActivityScheduleList.this, SeatBookingActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, coachSchedule.uid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_schedule_list;
    }

    @Override
    protected String getToolbarText() {
        return getString(R.string.activity_trip_list_title);
    }

    @Override
    protected void onDestroy() {
        mAdapter.cleanup();
        super.onDestroy();
    }
}
