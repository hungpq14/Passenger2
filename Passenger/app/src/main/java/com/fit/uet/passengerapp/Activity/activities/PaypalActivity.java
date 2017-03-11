package com.fit.uet.passengerapp.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;

public class PaypalActivity extends BaseToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_paypal;
    }

    @Override
    protected String getToolbarText() {
        return "Thanh toan ve";
    }
}
