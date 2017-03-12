package com.fit.uet.passengerapp.Activity.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.FavoriteCoachHostAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 11/03/2017.
 */

public class CoachHostFavoriteActivity extends BaseToolBarActivity {
    @BindView(R.id.recycler)
    RecyclerView mList;
    @BindView(R.id.empty_view)
    TextView mEmpty;
    FavoriteCoachHostAdapter mAapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String self = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mAapter = new FavoriteCoachHostAdapter(ref, ref.child(DB.COACH_HOST_FAV).child(self));
        mAapter.setupWithEmptyView(mEmpty);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coach_host_fav;
    }

    @Override
    protected String getToolbarText() {
        return getString(R.string.fav_coach_host);
    }

    @Override
    protected void onDestroy() {
        mAapter.cleanup();
        super.onDestroy();
    }
}
