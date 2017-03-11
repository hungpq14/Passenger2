package com.fit.uet.passengerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.Coach;
import com.fit.uet.passengerapp.models.CoachHost;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 07/03/2017.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> implements ChangeEventListener {
    public static final String TAG = "ScheduleAdapter";
    private ScheduleArray mSchedules;
    private OnItemClickListener mListener;

    public ScheduleAdapter(DatabaseReference ref, Query query) {
        mSchedules = new ScheduleArray(ref, query);
        mSchedules.setOnChangedListener(this);

    }

    public void setOnCLickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_schedule_item, parent, false);
        return new ScheduleHolder(view);
    }

    public void requery(Query query) {
        mSchedules.cleanup();
        notifyDataSetChanged();
        mSchedules.requery(query);
    }

    @Override
    public void onBindViewHolder(ScheduleHolder holder, int position) {
        CoachSchedule schedule = mSchedules.getItem(position);
        Coach coach = mSchedules.mCoachMap.get(schedule.coachUid);
        CoachHost coachHost = null;
        if (coach != null) {
            coachHost = mSchedules.mCoachHostMap.get(coach.coachHostUid);
        }
        holder.bind(schedule, coach, coachHost, mListener);

    }

    @Override
    public int getItemCount() {
        return mSchedules.getCount();
    }

    public void cleanup() {
        mSchedules.cleanup();
    }

    @Override
    public long getItemId(int position) {
        return mSchedules.getItem(position).uid.hashCode();
    }


    @Override
    public void onChildChanged(EventType type, int index, int oldIndex) {
        switch (type) {
            case ADDED:
                notifyItemInserted(index);
                break;
            case CHANGED:
                notifyItemChanged(index);
                break;
            case REMOVED:
                notifyItemRemoved(index);
                break;
            case MOVED:
                notifyItemMoved(oldIndex, index);
                break;
            default:
                throw new IllegalStateException("Incomplete case statement");
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onCancelled(DatabaseError error) {
        Log.w(TAG, error.toException());
    }


    public static class ScheduleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.departure_time)
        TextView mDepatureTimeView;
        @BindView(R.id.seat_valid_count)
        TextView mSeatCountView;
        @BindView(R.id.price)
        TextView mPriceView;
        @BindView(R.id.route)
        TextView mRouteView;
        @BindView(R.id.company_name)
        TextView mHostView;
        @BindView(R.id.rating)
        RatingBar mRattingBar;
        @BindView(R.id.coach)
        TextView mCoachNameView;

        private Context mContext;


        public ScheduleHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }


        public void bind(final CoachSchedule schedule, Coach coach, CoachHost coachHost, final OnItemClickListener listener) {
            String departureTime = DateTimeUtils.getTime(schedule.departureTime, false);
            mDepatureTimeView.setText(departureTime);
            mSeatCountView.setText(mContext.getString(R.string.format_seat_description, schedule.seatAvailable));
            if (coach != null) {
                mPriceView.setText(mContext.getString(R.string.format_ticket_price, schedule.costPerTicket));
                mRouteView.setText(mContext.getString(R.string.routeFormat, schedule.arriveFrom, schedule.arriveTo));
                mCoachNameView.setText(coach.kind + " chỗ");
            }

            if (coachHost != null) {
                mHostView.setText(coachHost.name);
                mRattingBar.setRating(coachHost.star);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(schedule);
                    }
                }
            });
        }

    }


    public interface OnItemClickListener {
        void onItemClick(CoachSchedule coachSchedule);
    }
}
