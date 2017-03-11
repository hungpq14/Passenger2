package com.fit.uet.passengerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.CoachSchedule;

import java.util.List;

/**
 * Created by phamh_000 on 11/03/2017.
 */

public class CoachManagerAdapter extends RecyclerView.Adapter<CoachManagerAdapter.CoachManagerViewHolder> {
    private List<CoachSchedule> coachSchedules;

    public CoachManagerAdapter(List<CoachSchedule> coachSchedules) {
        this.coachSchedules = coachSchedules;
    }

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CoachManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_coach_manager, parent, false);
        return new CoachManagerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoachManagerViewHolder holder, int position) {
        CoachSchedule schedule = coachSchedules.get(position);
        holder.bind(schedule, position);
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

        public void bind(CoachSchedule schedule, final int pos) {
            txtCoachTo.setText(schedule.arriveTo);
            txtPickTo.setText(schedule.pickTo);
            txtCoachFrom.setText(schedule.arriveFrom);
            txtPickFrom.setText(schedule.pickFrom);
            txtDepartureTime.setText("Khởi hành: " + schedule.departureTime);
            txtSeatAvail.setText("Số ghế trống: " + schedule.seatAvailable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}
