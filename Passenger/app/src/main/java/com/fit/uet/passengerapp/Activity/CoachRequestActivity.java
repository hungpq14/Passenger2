package com.fit.uet.passengerapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;

public class CoachRequestActivity extends BaseToolBarActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coach_request;
    }

    @Override
    protected String getToolbarText() {
        return "Yêu cầu chuyến";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
