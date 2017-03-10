package com.fit.uet.passengerapp.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.Message;
import com.fit.uet.passengerapp.models.User;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 10/03/2017.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageHolder> implements ChangeEventListener {
    public static final int INCOMING_MSG = R.layout.list_item_message_in;
    public static final int OUTGOING_MSG = R.layout.list_item_message_out;
    private static final String TAG = "MessageListAdapter";
    private User mDestination;
    FirebaseArray mData;
    private RecyclerView mHost;

    public MessageListAdapter(RecyclerView host, User destination, Query query) {
        this.mData = new FirebaseArray(query);
        mHost = host;
        mDestination = destination;
        mData.setOnChangedListener(this);

    }



    public Message getItem(int postion) {
        return mData.getItem(postion).getValue(Message.class);
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Message message = getItem(position);
        holder.messageView.setText(message.getMessage());
        holder.dateView.setText(DateTimeUtils.getLocalTime(message.getTimestamp()));

    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        return message.getSender().equals(mDestination.getUid()) ? INCOMING_MSG : OUTGOING_MSG;
    }


    @Override
    public int getItemCount() {
        return mData.getCount();
    }

    @Override
    public void onChildChanged(EventType type, int index, int oldIndex) {
        switch (type) {
            case ADDED:
                notifyItemInserted(index);
                mHost.scrollToPosition(index);
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

    public void cleanup() {
        mData.cleanup();
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_message)
        TextView messageView;
        @BindView(R.id.tv_date)
        TextView dateView;
        @BindView(R.id.iv_profile)
        @Nullable
        ImageView ivAvatar;

        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
