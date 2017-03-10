package com.fit.uet.passengerapp.Activity.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.Activity.activities.ActivityScheduleList;
import com.fit.uet.passengerapp.Activity.activities.ListCityActivity;
import com.fit.uet.passengerapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bien-kun on 05/03/2017.
 */

public class FilterFragment extends android.support.v4.app.Fragment implements DatePickerDialog.OnDateSetListener {
    public static final String LOCATION_FROM = "LOCATION_FROM";
    public static final String LOCATION_TO = "LOCATION_TO";
    public static final String TAG = "FilterFragment";
    public static final int INTENT_GET_TO = 0;
    public static final int INTENT_GET_FROM = 1;

    private int city_id_from, city_id_to;
    private DatePickerDialog mPicker;
    private Calendar mCalendar;
    private TextView mDateView;
    private View view;
    private LinearLayout inputLayoutFrom, inputLayoutTo;
    private TextView txtFrom, txtTo;
    FloatingActionButton btnFilter;
    View mDateContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_filter, container, false);
        initDraw();

        return view;
    }

    private void initDraw() {
        mDateView = (TextView) view.findViewById(R.id.tv_date);
        mCalendar = Calendar.getInstance();
        mPicker = new DatePickerDialog(getActivity(), this, mCalendar.get(Calendar.YEAR)
                , mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mPicker.getDatePicker().setMinDate(mCalendar.getTimeInMillis());
        mDateView.setText(DateUtils.formatDateTime(getActivity(), mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE));

        inputLayoutFrom = (LinearLayout) view.findViewById(R.id.layout_txt_from);
        inputLayoutTo = (LinearLayout) view.findViewById(R.id.layout_txt_to);
        txtFrom = (TextView) view.findViewById(R.id.txt_from);
        txtTo = (TextView) view.findViewById(R.id.txt_to);

        inputLayoutFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ListCityActivity.class), INTENT_GET_FROM);
            }
        });
        inputLayoutTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ListCityActivity.class), INTENT_GET_TO);
            }
        });

        btnFilter = (FloatingActionButton) view.findViewById(R.id.btn_filter);
        mDateContainer = view.findViewById(R.id.date_container);
        mDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPickerDialog();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityScheduleList.class);
//                intent.putExtra(LOCATION_FROM, edtFrom.getText().toString());
//                intent.putExtra(LOCATION_TO, edtTo.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        Log.d(TAG, requestCode + " " + resultCode);
        final int pos = data.getIntExtra("pos", 0);
        FirebaseDatabase.getInstance().getReference().child("city").child(pos + "").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (requestCode == INTENT_GET_FROM) {
                    city_id_from = pos;
                    txtFrom.setText(dataSnapshot.getValue(String.class));
                } else if (requestCode == INTENT_GET_TO) {
                    city_id_to = pos;
                    txtTo.setText(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void showPickerDialog() {
        mPicker.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mCalendar.set(year, month, day);
        mDateView.setText(DateUtils.formatDateTime(getActivity(), mCalendar.getTime().getTime(), DateUtils.FORMAT_SHOW_DATE));
    }
}
