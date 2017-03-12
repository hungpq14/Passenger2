package com.fit.uet.passengerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fit.uet.passengerapp.ListenerEvent.EventPickLocation;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.City;
import com.fit.uet.passengerapp.models.CoachRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by phamh_000 on 12/03/2017.
 */

public class MyCoachRequestAdapter extends RecyclerView.Adapter<MyCoachRequestAdapter.MyCoachViewHolder> {
    private ArrayList<CoachRequest> coachRequests;

    public MyCoachRequestAdapter(ArrayList<CoachRequest> coachRequests) {
        this.coachRequests = coachRequests;
    }

    @Override
    public MyCoachViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_coach_request, parent, false);
        return new MyCoachViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyCoachViewHolder holder, int position) {
        CoachRequest coachRequest = coachRequests.get(position);
        holder.bind(coachRequest, position);
    }

    @Override
    public int getItemCount() {
        return coachRequests.size();
    }

    public class MyCoachViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFrom, txtTo, txtMax, txtMin;
        private Button btnCancel;

        public MyCoachViewHolder(View itemView) {
            super(itemView);
            txtFrom = (TextView) itemView.findViewById(R.id.txt_from);
            txtTo = (TextView) itemView.findViewById(R.id.txt_to);
            txtMax = (TextView) itemView.findViewById(R.id.txt_max_date);
            txtMin = (TextView) itemView.findViewById(R.id.txt_min_date);
            btnCancel = (Button) itemView.findViewById(R.id.btn_cancel);
        }

        public void bind(CoachRequest coachRequest, final int pos) {
            txtMax.setText(coachRequest.timeMaxToStart);
            txtMin.setText(coachRequest.timeMinToStart);
            txtFrom.setText(coachRequest.arriveFrom);
            txtTo.setText(coachRequest.arriveTo);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventPickLocation(pos));
                }
            });
        }
    }
}
