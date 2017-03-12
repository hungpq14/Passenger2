package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.CommentAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.CoachHost;
import com.fit.uet.passengerapp.models.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoachHostActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {
    @BindView(R.id.name)
    TextView mNameView;
    @BindView(R.id.rating_bar)
    RatingBar mRating;
    @BindView(R.id.description)
    TextView mDescriptionView;
    @BindView(R.id.arrive_count)
    TextView arriveCountView;
    @BindView(R.id.logo)
    ImageView mLogoView;
    @BindView(R.id.comments)
    RecyclerView mCommentList;
    @BindView(R.id.btn_save)
    Button mSaveView;
    @BindView(R.id.btn_website)
    Button mWebsite;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public static final String KEY_HOST_ID = "host_id";


    private String coachHostUid;
    private StorageReference mStorage;
    private CommentAdapter mAdapter;
    private DatabaseReference mRef;
    private CoachHost mCoachHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        coachHostUid = getIntent().getStringExtra(KEY_HOST_ID);
        mStorage = FirebaseStorage.getInstance().getReference();

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child(DB.COACH_HOST).child(coachHostUid).addListenerForSingleValueEvent(this);
        mSaveView.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void updateUI(CoachHost coachHost) {
        StorageReference ref = mStorage.child(coachHost.logo);
        mNameView.setText(coachHost.name);
        getSupportActionBar().setTitle(coachHost.name);
        mRating.setRating(coachHost.star);
        mDescriptionView.setText(coachHost.description);
        arriveCountView.setText(getString(R.string.format_arrrive_count, coachHost.numArrived));
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(ref)
                .centerCrop()
                .crossFade()
                .into(mLogoView);
        mCommentList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentAdapter(Comment.class, R.layout.comment_list_item,
                CommentAdapter.CommentHolder.class, mRef.child(DB.COMMENTS).orderByChild("coachHostUid").equalTo(coachHostUid));
        mCommentList.setAdapter(mAdapter);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        CoachHost coachHost = dataSnapshot.getValue(CoachHost.class);
        mCoachHost = coachHost;
        if (coachHost != null) {
            updateUI(coachHost);
        }
    }

    @Override
    protected void onDestroy() {
        mAdapter.cleanup();
        super.onDestroy();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("CoachHostActivity", databaseError.getMessage(), databaseError.toException());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                String self = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference()
                        .child(DB.COACH_HOST_FAV).child(self).child(coachHostUid).setValue(true);
                Snackbar.make(view, "Đã lưu vào danh sách ưa thích", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.fab:
                if (mCoachHost != null) {
                    Intent intent = new Intent(this, MessageListActivity.class);
                    intent.putExtra(MessageListActivity.KEY_DESTINATION, mCoachHost.hostUid);
                    startActivity(intent);
                }
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coach_host,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_report){
            startActivity(new Intent(this,ReportActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
