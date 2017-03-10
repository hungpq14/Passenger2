package com.fit.uet.passengerapp.Activity.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fit.uet.passengerapp.Activity.fragments.ScannerFragment;
import com.fit.uet.passengerapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketBoxActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.btn_barcode_scan)
    FloatingActionButton btn_barcode_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_box);
        ButterKnife.bind(this);

        btn_barcode_scan.setOnClickListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    btn_barcode_scan.setVisibility(View.GONE);
                } else {
                    btn_barcode_scan.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_barcode_scan:
                scan();
        }
    }

    private void scan() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ScannerFragment.newInstance()).addToBackStack(null).commit();
    }

}
