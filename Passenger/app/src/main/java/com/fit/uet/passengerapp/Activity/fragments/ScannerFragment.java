package com.fit.uet.passengerapp.Activity.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit.uet.passengerapp.Activity.activities.TicketBoxActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.Ticket;
import com.fit.uet.passengerapp.utils.DialogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private TicketBoxActivity activity;

    @BindView(R.id.scanner_view)
    ZXingScannerView scanner_view;

    public static ScannerFragment newInstance() {
        return new ScannerFragment();
    }

    public ScannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (TicketBoxActivity) getActivity();

        scanner_view.setResultHandler(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        scanner_view.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanner_view.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
//        if (checkBarcode(result.getText())) {
//            activity.getSupportFragmentManager().popBackStack();
//            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, TicketInfoFragment.newInstance(result.getText())).addToBackStack(null).commit();
//        } else {
//            // Vé không tồn tại trên chuyến xe này
//            DialogUtils.showAlertDialog(activity, "", "Vé không tồn tại!", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    scanner_view.resumeCameraPreview(ScannerFragment.this);
//                }
//            });
//        }

        checkBarcode(result.getText());
    }

    private boolean checkBarcode(final String code) {
        DatabaseReference databaseReference = activity.getDatabaseReference();
        databaseReference.child(Ticket.CHILD_TICKET).orderByChild("coach_schedule_id").equalTo(activity.getIntent().getStringExtra(Intent.EXTRA_TEXT)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (code.equals(snapshot.getKey())) {
                        // Xe tồn tại vé này
                        Ticket ticket = snapshot.getValue(Ticket.class);
                        if (ticket.checkout) { // Vé đã đc kiểm tra
                            DialogUtils.showAlertDialog(activity, "", "Vé đã được kiểm tra!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    scanner_view.resumeCameraPreview(ScannerFragment.this);
                                }
                            });
                        } else {
                            activity.getSupportFragmentManager().popBackStack();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, TicketInfoFragment.newInstance(code)).addToBackStack(null).commit();
                        }
                        return;
                    }
                }

                // Vé không tồn tại trên chuyến xe này
                DialogUtils.showAlertDialog(activity, "", "Vé không tồn tại!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scanner_view.resumeCameraPreview(ScannerFragment.this);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return code.equals(activity.getIntent().getStringExtra(Intent.EXTRA_TEXT));
    }

    @Override
    public void onStop() {
        super.onStop();
        scanner_view.stopCamera();
    }
}
