package com.fit.uet.passengerapp.Activity.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.activities.CartActivity;
import com.fit.uet.passengerapp.Activity.activities.CoachHostFavoriteActivity;
import com.fit.uet.passengerapp.Activity.activities.CoachManagerActivity;
import com.fit.uet.passengerapp.Activity.activities.CoachRequestActivity;
import com.fit.uet.passengerapp.Activity.activities.ConversationActivity;
import com.fit.uet.passengerapp.Activity.activities.ForceInputPhoneNumActivity;
import com.fit.uet.passengerapp.Activity.activities.MyCoachRequestManagerActivity;
import com.fit.uet.passengerapp.Activity.activities.SendRequestCoachActivity;
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
    private RelativeLayout layoutSignOut, layoutCoachManager, layoutTickerMan, layoutCoachLove,
            layoutChangePhoneNum, layoutMessage, layoutCoachRequest, layoutSendCoachRequest, layoutMyCoachRequest;
    private TextView txtUserName;
    private TextView txtTrustPoint;
    SharedPreferences sharedPreferences;

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
        layoutMessage = (RelativeLayout) view.findViewById(R.id.layout_message);
        layoutCoachRequest = (RelativeLayout) view.findViewById(R.id.layout_coach_request_pending);
        layoutSendCoachRequest = (RelativeLayout) view.findViewById(R.id.layout_coach_request);
        layoutMyCoachRequest = (RelativeLayout) view.findViewById(R.id.layout_man_my_coach_request);

        layoutSignOut.setOnClickListener(navClickItem);
        layoutCoachManager.setOnClickListener(navClickItem);
        layoutTickerMan.setOnClickListener(navClickItem);
        layoutCoachLove.setOnClickListener(navClickItem);
        layoutChangePhoneNum.setOnClickListener(navClickItem);
        layoutMessage.setOnClickListener(navClickItem);
        layoutCoachRequest.setOnClickListener(navClickItem);
        layoutSendCoachRequest.setOnClickListener(navClickItem);
        layoutMyCoachRequest.setOnClickListener(navClickItem);

        // type_user = true -> coach_host
        sharedPreferences = getActivity().getSharedPreferences("share", Context.MODE_PRIVATE);
        if (!sharedPreferences.contains("type_user")) {
            checkIfHasCoachMan();
        } else {
            if (!sharedPreferences.getBoolean("type_user", true)) {
                layoutCoachManager.setVisibility(View.GONE);
                layoutCoachRequest.setVisibility(View.GONE);
            }
        }
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
                int point = 0;
                try {
                    point = dataSnapshot.getValue(Integer.class);
                } catch (Exception ignored) {

                }
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
                    sharedPreferences.edit().putBoolean("type_user", false).commit();
                    layoutCoachManager.setVisibility(View.GONE);
                } else {
                    sharedPreferences.edit().putBoolean("type_user", true).commit();
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
                getActivity().getSharedPreferences("share", Context.MODE_PRIVATE).edit().remove("type_user").commit();
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
            } else if (v.equals(layoutMessage)) {
                startActivity(new Intent(getContext(), ConversationActivity.class));
            } else if (v.equals(layoutCoachRequest)) {
                startActivity(new Intent(getContext(), CoachRequestActivity.class));
            } else if (v.equals(layoutSendCoachRequest)) {
                startActivity(new Intent(getContext(), SendRequestCoachActivity.class));
            } else if (v.equals(layoutMyCoachRequest)) {
                startActivity(new Intent(getContext(), MyCoachRequestManagerActivity.class));
            }
        }
    };
}
