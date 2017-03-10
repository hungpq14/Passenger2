package com.fit.uet.passengerapp.Activity.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.SeatsAdapter;
import com.fit.uet.passengerapp.models.AbstractItem;
import com.fit.uet.passengerapp.models.CenterItem;
import com.fit.uet.passengerapp.models.EdgeItem;
import com.fit.uet.passengerapp.models.EmptyItem;
import com.fit.uet.passengerapp.models.OrderedItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeatBookingActivity extends BaseToolBarActivity {

    @BindView(R.id.rv_seats)
    RecyclerView rv_seats;

    @BindView(R.id.tv_seats)
    TextView tv_seats;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        int COLUMNS = 5;

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        rv_seats.setLayoutManager(manager);

        List<AbstractItem> items = new ArrayList<>();
        for (int i=0; i<30; i++) {

            if (i%COLUMNS==0 || i%COLUMNS==4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i%COLUMNS==1 || i%COLUMNS==3) {
                items.add(new CenterItem(String.valueOf(i)));
            } else {
                items.add(new EmptyItem(String.valueOf(i)));
            }
        }
        items.add(new OrderedItem(String.valueOf(30)));
        items.add(new OrderedItem(String.valueOf(31)));
        items.add(new EmptyItem(String.valueOf(32)));
        items.add(new OrderedItem(String.valueOf(33)));
        items.add(new OrderedItem(String.valueOf(34)));

        final SeatsAdapter adapter = new SeatsAdapter(this, items);
        rv_seats.setAdapter(adapter);

        adapter.setmOnSeatSelected(new SeatsAdapter.OnSeatSelected() {
            @Override
            public void onSeatSelected(int count) {
                if (count != 0) {
                    tv_seats.setText(adapter.getSelectedItems().toString());
                } else {
                    tv_seats.setText("");
                }
                tv_price.setText(count * 5 + "$");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seat_booking;
    }

    @Override
    protected String getToolbarText() {
        return "Booking";
    }
}
