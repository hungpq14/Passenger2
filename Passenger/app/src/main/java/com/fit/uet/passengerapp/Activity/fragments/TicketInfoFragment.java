package com.fit.uet.passengerapp.Activity.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.activities.TicketBoxActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.utils.BarcodeUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TicketInfoFragment extends Fragment implements View.OnClickListener{
    private static final String KEY_CODE = "CODE";

    private TicketBoxActivity activity;

    @BindView(R.id.img_barcode)
    ImageView img_barcode;

    @BindView(R.id.tv_code)
    TextView tv_code;

    @BindView(R.id.tv_go_back)
    TextView tv_go_back;

    @BindView(R.id.tv_accept)
    TextView tv_accept;

    public static TicketInfoFragment newInstance(String code) {
        TicketInfoFragment fragment = new TicketInfoFragment();
        Bundle arg = new Bundle();
        arg.putString(KEY_CODE, code);
        fragment.setArguments(arg);
        return fragment;
    }

    public TicketInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (TicketBoxActivity) getActivity();

        init(getArguments().getString(KEY_CODE));
    }

    private void init(String code) {
        tv_go_back.setOnClickListener(this);
        tv_accept.setOnClickListener(this);

        // Lấy thông tin vé từ service

        tv_code.setText(code);

        Bitmap barcodeBitmap;
        try {
            barcodeBitmap = BarcodeUtils.encodeAsBitmap(code, BarcodeFormat.CODE_128, 400, 100);
            img_barcode.setImageBitmap(barcodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_back:
                activity.getSupportFragmentManager().popBackStack();
                return;
            case R.id.tv_accept:
                activity.getSupportFragmentManager().popBackStack();
        }
    }
}
