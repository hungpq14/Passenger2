package com.fit.uet.passengerapp.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;

public class CoachManagerActivity extends BaseToolBarActivity {
    private RecyclerView recyclerView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_coach_manager;
    }

    @Override
    protected String getToolbarText() {
        return "Quản lý chuyến xe";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDraw();
    }

    private void initDraw() {

    }
}
