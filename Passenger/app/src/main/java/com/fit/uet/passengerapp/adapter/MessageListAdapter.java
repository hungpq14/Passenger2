package com.fit.uet.passengerapp.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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
    private TextDrawable.IBuilder mBuilder;

    public MessageListAdapter(RecyclerView host, User destination, Query query) {
        this.mData = new FirebaseArray(query);
        mHost = host;
        mDestination = destination;
        mData.setOnChangedListener(this);
        mBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(2)
                .endConfig()
                .round();


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
        boolean visible = true;

        int viewType = getItemViewType(position);

        holder.dateView.setVisibility(View.VISIBLE);
        holder.messageView.setText(message.getMessage());
        if (position < mData.getCount() - 1 && position > 0) {
            if (viewType == getItemViewType(position + 1)) {
                visible = false;
                holder.dateView.setVisibility(View.GONE);
            }
        }

        if (viewType == INCOMING_MSG) {
            String sender = mDestination.getName();
            int color = ColorGenerator.MATERIAL.getColor(sender);
            holder.ivAvatar.setImageDrawable(mBuilder.build(sender.substring(0, 1), color));
        }

        if (visible) {
            holder.dateView.setText(DateTimeUtils.getLocalTime(message.getTimestamp()));
        }

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
                if (index > 0) {
                    notifyItemChanged(index - 1);
                }
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
