package com.fit.uet.passengerapp.Activity.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit.uet.passengerapp.Activity.activities.TicketBoxActivity;
import com.fit.uet.passengerapp.R;
import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler{
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
        if (checkBarcode(result.getText())) {
            activity.getSupportFragmentManager().popBackStack();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, TicketInfoFragment.newInstance(result.getText())).addToBackStack(null).commit();
        } else {
            // Vé không tồn tại trên chuyến xe này
        }
    }

    private boolean checkBarcode(String code) {
        return true;
    }


}
