package com.fit.uet.passengerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fit.uet.passengerapp.Activity.activities.CoachHostActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.Coach;
import com.fit.uet.passengerapp.models.CoachHost;
import com.fit.uet.passengerapp.models.CoachSchedule;
import com.fit.uet.passengerapp.models.Ticket;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by phamtruong on 3/11/17.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Ticket> tickets;
    private DatabaseReference databaseReference;

    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onReviewClick(String coachHostUid);

        void onFinishGetData();
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CartAdapter(Context context, List<Ticket> tickets, DatabaseReference databaseReference) {
        this.context = context;
        this.tickets = tickets;
        this.databaseReference = databaseReference;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.card_history_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Ticket ticket = tickets.get(position);

        if (ticket.checkout) {
            holder.tv_seats.setText("Đã xác nhận");
        } else {
            holder.tv_seats.setText("Chưa xác nhận");
        }

        databaseReference.child(DB.SCHEDULE).child(ticket.coach_schedule_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CoachSchedule schedule = dataSnapshot.getValue(CoachSchedule.class);

                long ms = DateTimeUtils.getMillisFromString(schedule.departureTime);
                String dateTime = DateTimeUtils.dateStringFormat(ms) + " - " + DateTimeUtils.getTimeFromMs(ms);
                holder.departure_time.setText(dateTime);

                holder.price.setText(ticket.seats.size() * schedule.costPerTicket + " đồng");

                holder.route.setText("Từ " + schedule.arriveFrom + " tới " + schedule.arriveTo);


                databaseReference.child(DB.COACH).child(schedule.coachUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Coach coach = dataSnapshot.getValue(Coach.class);

                        databaseReference.child(DB.COACH_HOST).child(coach.coachHostUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                CoachHost coachHost = dataSnapshot.getValue(CoachHost.class);

                                holder.company_name.setText(coachHost.name);
                                holder.company_name.setTag(coachHost.uid);
                                holder.ratting.setRating(coachHost.star);

                                if (listener != null) listener.onFinishGetData();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("Cancel", databaseError.getMessage());
                            }
                        });

                        holder.coach_host_container.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, CoachHostActivity.class);
                                intent.putExtra(CoachHostActivity.KEY_HOST_ID, coach.coachHostUid);
                                context.startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView departure_time;
        public TextView price;
        public TextView route;
        public TextView tv_seats;
        public TextView company_name;
        public Button preview;
        public View coach_host_container;
        public RatingBar ratting;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            departure_time = (TextView) itemView.findViewById(R.id.departure_time);
            price = (TextView) itemView.findViewById(R.id.price);
            route = (TextView) itemView.findViewById(R.id.route);
            tv_seats = (TextView) itemView.findViewById(R.id.tv_seats);
            company_name = (TextView) itemView.findViewById(R.id.company_name);
            preview = (Button) itemView.findViewById(R.id.review);
            coach_host_container = itemView.findViewById(R.id.coach_host_container);
            ratting = (RatingBar) itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onItemClick(getLayoutPosition());

                }
            });
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onReviewClick((String) company_name.getTag());
                }
            });
        }
    }

}
