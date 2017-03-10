package com.fit.uet.passengerapp.Activity.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;

import butterknife.BindView;

public class CoachHostActivity extends AppCompatActivity {
    @BindView(R.id.name)
    TextView nameView;
    @BindView(R.id.rating)
    RatingBar mRating;
    @BindView(R.id.description)
    TextView descriptionView;
    @BindView(R.id.arrive_count)
    TextView arriveCountView;
    @BindView(R.id.logo)
    ImageView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
    }
}
