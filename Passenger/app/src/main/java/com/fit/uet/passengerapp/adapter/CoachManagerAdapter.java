package com.fit.uet.passengerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.CoachSchedule;

import java.util.ArrayList;

/**
 * Created by phamh_000 on 11/03/2017.
 */

public class CoachManagerAdapter extends RecyclerView.Adapter<CoachManagerAdapter.CoachManagerViewHolder> {
    private ArrayList<CoachSchedule> coachSchedules;

    public CoachManagerAdapter(ArrayList<CoachSchedule> coachSchedules) {
        this.coachSchedules = coachSchedules;
    }

    @Override
    public CoachManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_coach_manager, parent, false);
        return new CoachManagerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoachManagerViewHolder holder, int position) {
        CoachSchedule schedule = coachSchedules.get(position);
        holder.bind(schedule);
    }

    @Override
    public int getItemCount() {
        return coachSchedules.size();
    }

    public class CoachManagerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCoachFrom, txtCoachTo, txtPickFrom, txtPickTo;
        private TextView txtSeatAvail;
        private TextView txtDepartureTime;
        private View itemView;

        public CoachManagerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtCoachFrom = (TextView) itemView.findViewById(R.id.txt_arrive_from);
            txtCoachTo = (TextView) itemView.findViewById(R.id.txt_arrive_to);
            txtPickFrom = (TextView) itemView.findViewById(R.id.txt_pick_from);
            txtPickTo = (TextView) itemView.findViewById(R.id.txt_pick_to);
            txtSeatAvail = (TextView) itemView.findViewById(R.id.txt_seat_avail);
            txtDepartureTime = (TextView) itemView.findViewById(R.id.txt_departure_time);
        }

        public void bind(CoachSchedule schedule) {
            txtCoachTo.setText(schedule.arriveTo);
            txtPickTo.setText(schedule.pickTo);
            txtCoachFrom.setText(schedule.arriveFrom);
            txtPickFrom.setText(schedule.pickFrom);
            txtDepartureTime.setText("Khởi hành: " + schedule.departureTime);
            txtSeatAvail.setText("Số ghế đã đặt: " + schedule.seatAvailable);
        }
    }
}
