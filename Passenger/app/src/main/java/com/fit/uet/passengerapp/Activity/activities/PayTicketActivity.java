package com.fit.uet.passengerapp.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;

public class PayTicketActivity extends BaseToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDraw();
    }

    private void initDraw() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_ticket;
    }

    @Override
    protected String getToolbarText() {
        return "Thanh Toán Vé";
    }
}
