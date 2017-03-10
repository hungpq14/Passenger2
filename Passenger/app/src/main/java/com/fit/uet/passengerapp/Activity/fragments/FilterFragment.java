package com.fit.uet.passengerapp.Activity.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
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
import com.fit.uet.passengerapp.R;

import java.util.Calendar;

/**
 * Created by Bien-kun on 05/03/2017.
 */

public class FilterFragment extends android.support.v4.app.Fragment implements DatePickerDialog.OnDateSetListener {
    public static final String LOCATION_FROM = "LOCATION_FROM";
    public static final String LOCATION_TO = "LOCATION_TO";
    public static final String TAG = "FilterFragment";

    private DatePickerDialog mPicker;
    private Calendar mCalendar;
    private TextView mDateView;
    private View view;
    private LinearLayout inputLayoutFrom, inputLayoutTo;
    FloatingActionButton btnFilter;
    EditText edtFrom, edtTo;

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

        inputLayoutFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click from");
            }
        });

        btnFilter = (FloatingActionButton) view.findViewById(R.id.btn_filter);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityScheduleList.class);
                intent.putExtra(LOCATION_FROM, edtFrom.getText().toString());
                intent.putExtra(LOCATION_TO, edtTo.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void showPickerDialog(View view) {
        mPicker.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mCalendar.set(year, month, day);
        mDateView.setText(DateUtils.formatDateTime(getActivity(), mCalendar.getTime().getTime(), DateUtils.FORMAT_SHOW_DATE));
    }
}
