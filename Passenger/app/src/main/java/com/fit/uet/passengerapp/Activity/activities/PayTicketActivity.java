package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PayTicketActivity extends BaseToolBarActivity {

    RadioButton radioPay1, radioPay2, radioPay3;
    Button btnPay;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDraw();
    }

    private void initDraw() {
        radioPay1 = (RadioButton) findViewById(R.id.radio_pay_option_0);
        radioPay2 = (RadioButton) findViewById(R.id.radio_pay_option_1);
        radioPay3 = (RadioButton) findViewById(R.id.radio_pay_option_2);
        radioGroup = (RadioGroup) findViewById(R.id.payGroup);
        btnPay = (Button) findViewById(R.id.btn_pay);

        checkTrustPoint();
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = getRadioState();
                Intent intent = new Intent(PayTicketActivity.this, PaypalActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    private void checkTrustPoint() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("point").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int point = dataSnapshot.getValue(Integer.class);
                if (point < 8) {
                    radioPay1.setEnabled(false);
                } else radioPay1.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public int getRadioState() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_pay_option_0: {
                return 0;
            }
            case R.id.radio_pay_option_1: {
                return 1;
            }
            case R.id.radio_pay_option_2: {
                return 2;
            }
            default: {
                return 1;
            }
        }
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
