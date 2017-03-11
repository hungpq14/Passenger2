package com.fit.uet.passengerapp.Activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.activities.CartActivity;
import com.fit.uet.passengerapp.Activity.activities.CoachHostFavoriteActivity;
import com.fit.uet.passengerapp.Activity.activities.CoachManagerActivity;
import com.fit.uet.passengerapp.Activity.activities.ForceInputPhoneNumActivity;
import com.fit.uet.passengerapp.Activity.activities.SignInActivity;
import com.fit.uet.passengerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigationFragment extends Fragment {
    private View view;
    private RelativeLayout layoutSignOut, layoutCoachManager, layoutTickerMan, layoutCoachLove, layoutChangePhoneNum;
    private TextView txtUserName;
    private TextView txtTrustPoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_navigation, container, false);

        initDraw();

        return view;
    }

    private void initDraw() {
        layoutSignOut = (RelativeLayout) view.findViewById(R.id.layout_sign_out);
        layoutCoachManager = (RelativeLayout) view.findViewById(R.id.layout_coach_manager);
        layoutTickerMan = (RelativeLayout) view.findViewById(R.id.layout_ticket_man);
        layoutCoachLove = (RelativeLayout) view.findViewById(R.id.layout_coach_love);
        layoutChangePhoneNum = (RelativeLayout) view.findViewById(R.id.layout_change_phone_num);
        layoutSignOut.setOnClickListener(navClickItem);
        layoutCoachManager.setOnClickListener(navClickItem);
        layoutTickerMan.setOnClickListener(navClickItem);
        layoutCoachLove.setOnClickListener(navClickItem);
        layoutChangePhoneNum.setOnClickListener(navClickItem);

        checkIfHasCoachMan();
        checkTrustPoint();

        txtUserName = (TextView) view.findViewById(R.id.txt_user_name);
        txtTrustPoint = (TextView) view.findViewById(R.id.txt_trust_point);

        txtUserName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    private void checkTrustPoint() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("point").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int point = dataSnapshot.getValue(Integer.class);
                txtTrustPoint.setText(point + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void checkIfHasCoachMan() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("coachUid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    layoutCoachManager.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    View.OnClickListener navClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(layoutSignOut)) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SignInActivity.class));
            } else if (v.equals(layoutCoachManager)) {
                startActivity(new Intent(getContext(), CoachManagerActivity.class));
            } else if (v.equals(layoutTickerMan)) {
                startActivity(new Intent(getContext(), CartActivity.class));
            } else if (v.equals(layoutCoachLove)) {
                startActivity(new Intent(getContext(), CoachHostFavoriteActivity.class));
            } else if (v.equals(layoutChangePhoneNum)) {
                Intent intent = new Intent(getContext(), ForceInputPhoneNumActivity.class);
                intent.putExtra("type", ForceInputPhoneNumActivity.TYPE_CHANGE_PHONE_NUM);
                startActivity(intent);
                getActivity().finish();
            }
        }
    };
}
