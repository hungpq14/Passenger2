package com.fit.uet.passengerapp.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;

public class PaypalActivity extends BaseToolBarActivity {
    public static final int TYPE_SHIP = 0;
    public static final int TYPE_PAYPAL = 1;
    private CardView cardShip, cardPaypal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDraw();
    }

    private void initDraw() {
        cardPaypal = (CardView) findViewById(R.id.layout_paypal);
        cardShip = (CardView) findViewById(R.id.layout_pay_with_ship);
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
