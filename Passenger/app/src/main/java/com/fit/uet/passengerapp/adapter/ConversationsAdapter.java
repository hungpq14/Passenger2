package com.fit.uet.passengerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.Conversation;
import com.fit.uet.passengerapp.models.Message;
import com.fit.uet.passengerapp.models.User;
import com.fit.uet.passengerapp.ui.CircleImageView;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 10/03/2017.
 */

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationHolder> implements ChangeEventListener {
    public static final String TAG = "ConversationsAdapter";
    private ConversationsArray mConversations;
    private OnItemClickListener mListener;

    public ConversationsAdapter(DatabaseReference ref, Query query, String self) {
        mConversations = new ConversationsArray(ref, query, self);
        mConversations.setOnChangedListener(this);

    }




    @Override
    public ConversationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_list_item, parent, false);

        return new ConversationHolder(view);
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(ConversationHolder holder, int position) {
        holder.bind(mConversations.getItem(position),mListener);
    }

    public int getItemCount() {
        return mConversations.getCount();
    }

    public void cleanup() {
        mConversations.cleanup();
    }

    @Override
    public long getItemId(int position) {
        return mConversations.getItem(position).getKey().hashCode();
    }


    @Override
    public void onChildChanged(ChangeEventListener.EventType type, int index, int oldIndex) {
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


    public static class ConversationHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_profile)
        CircleImageView profileImageView;
        @BindView(R.id.tv_name)
        TextView nameTextView;
        @BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.tv_time)
        TextView timeTextView;

        public ConversationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final Conversation conversation, final OnItemClickListener listener) {
            User user = conversation.getUser();
            Message message = conversation.getMessage();
            if (user != null) {
                nameTextView.setText(user.getName());
            }
            if (message != null) {
                messageTextView.setText(message.getMessage());
                timeTextView.setText(DateTimeUtils.getLocalTime(message.getTimestamp()));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemClick(conversation);
                    }
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(Conversation conversation);
    }
}
