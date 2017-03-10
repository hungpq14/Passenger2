package com.fit.uet.passengerapp.Activity.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.CoachHost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoachHostActivity extends AppCompatActivity implements ValueEventListener {
    @BindView(R.id.name)
    TextView mNameView;
    @BindView(R.id.rating)
    RatingBar mRating;
    @BindView(R.id.description)
    TextView mDescriptionView;
    @BindView(R.id.arrive_count)
    TextView arriveCountView;
    @BindView(R.id.logo)
    ImageView mLogoView;
    public static final String KEY_HOST_ID = "host_id";

    private String coachHostUid;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        coachHostUid = getIntent().getStringExtra(KEY_HOST_ID);
        mStorage = FirebaseStorage.getInstance().getReference();


        LayerDrawable stars = (LayerDrawable) mRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        FirebaseDatabase.getInstance().getReference().child(DB.COACH_HOST).child(coachHostUid).addValueEventListener(this);
    }

    private void updateUI(CoachHost coachHost) {
        StorageReference ref = mStorage.child(coachHost.url);
        mNameView.setText(coachHost.name);
        mRating.setRating(coachHost.star);
        mDescriptionView.setText(coachHost.description);
        arriveCountView.setText(getString(R.string.format_arrrive_count, coachHost.numArrived));
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(ref)
                .centerCrop()
                .crossFade()
                .into(mLogoView);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        CoachHost coachHost = dataSnapshot.getValue(CoachHost.class);
        if (coachHost != null) {
            updateUI(coachHost);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("CoachHostActivity", databaseError.getMessage(), databaseError.toException());
    }
}
