package com.fit.uet.passengerapp.adapter;

import android.util.Log;

import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.Conversation;
import com.fit.uet.passengerapp.models.Message;
import com.fit.uet.passengerapp.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Bien-kun on 09/03/2017.
 */

public class ConversationsArray implements ChildEventListener {
    private DatabaseReference mRef;
    private Query mQuery;
    private ChangeEventListener mListener;
    private HashMap<Query, ValueEventListener> mUserMap;
    private HashMap<Query, ValueEventListener> mLastMessageMap;
    private String mSelf;
    private List<Conversation> mConversations;

    public ConversationsArray(DatabaseReference ref, Query query, String self) {
        mSelf = self;
        mRef = ref;
        mQuery = query;
        mConversations = new ArrayList<>();
        mQuery.addChildEventListener(this);
        mUserMap = new HashMap<>();
        mLastMessageMap = new HashMap<>();
    }


    public Conversation getItem(int position) {
        return mConversations.get(position);
    }

    public int getCount() {
        return mConversations.size();
    }


    private boolean isKeyAtIndex(String key, int index) {
        return index >= 0 && index < getCount() && mConversations.get(index).getKey().equals(key);
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (Conversation snapshot : mConversations) {
            if (snapshot.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        int index = 0;
        if (previousChildKey != null) {
            index = getIndexForKey(previousChildKey) + 1;
        }
        String destination = snapshot.getKey();
        mConversations.add(index, new Conversation(destination));

        Query userQuery = mRef.child(DB.USER).child(destination);
        mUserMap.put(userQuery, userQuery.addValueEventListener(new UserEventListener()));

        Query msgQuery = DB.getLastMessageForUserRef(mRef, mSelf, destination);

        mLastMessageMap.put(msgQuery, msgQuery.addValueEventListener(new LastMessageEventListener(destination)));
        notifyChangedListeners(ChangeEventListener.EventType.ADDED, index);

    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
        Log.w("ConversationsArray", "onChildChanged: this data should not changed");
        // we don't expect this happen
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
        int index = getIndexForKey(snapshot.getKey());
        Log.w("ConversationsArray", "onChildRemoved: this data should not removed");
        // we don't expect this happen
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        //TODO store or query again to get coach data
        Log.i("ConversationsArray", "onChildMoved");
        String key = snapshot.getKey();
        int oldIndex = getIndexForKey(snapshot.getKey());
        Conversation conversation = mConversations.remove(oldIndex);
        int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
        mConversations.add(newIndex, conversation);
        notifyChangedListeners(ChangeEventListener.EventType.MOVED, newIndex, oldIndex);
    }


    @Override
    public void onCancelled(DatabaseError error) {
        notifyCancelledListeners(error);
    }

    public void setOnChangedListener(ChangeEventListener listener) {
        mListener = listener;
    }

    protected void notifyChangedListeners(ChangeEventListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(ChangeEventListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChildChanged(type, index, oldIndex);
        }
    }

    protected void notifyCancelledListeners(DatabaseError error) {
        if (mListener != null) {
            mListener.onCancelled(error);
        }
    }

    public void cleanup() {
        mQuery.removeEventListener(this);
        Set<Query> refs = new HashSet<>(mLastMessageMap.keySet());
        for (Query ref : refs) {
            ref.removeEventListener(mLastMessageMap.remove(ref));
        }

        Set<Query> userRefs = new HashSet<>(mUserMap.keySet());
        for (Query userRef : userRefs) {
            userRef.removeEventListener(mUserMap.remove(userRef));
        }
    }


    private class UserEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot snapshot) {
            String key = snapshot.getKey();
            int index = getIndexForKey(key);
            User user = snapshot.getValue(User.class);
            Conversation conversation = mConversations.get(index);
            if (snapshot.getValue() != null) {
                conversation.setUser(user);
                notifyChangedListeners(ChangeEventListener.EventType.CHANGED, index);

            } else {
                if (isKeyAtIndex(key, index)) {
                    mConversations.remove(index);
                    notifyChangedListeners(ChangeEventListener.EventType.REMOVED, index);
                } else {
                    Log.w("ConversationsArray", "Key not found at ref: " + snapshot.getRef());
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            notifyCancelledListeners(databaseError);
        }
    }

    private class LastMessageEventListener implements ValueEventListener {
        private String key;

        public LastMessageEventListener(String key) {
            this.key = key;
        }

        @Override
        public void onDataChange(DataSnapshot snapshot) {

            int index = getIndexForKey(key);
            Conversation conversation = mConversations.get(index);
            if (snapshot.getValue() != null && snapshot.hasChildren()) {
                Iterator<DataSnapshot> children = snapshot.getChildren().iterator();
                DataSnapshot child = children.next();

                Message message = child.getValue(Message.class);
                if (conversation.getMessage() == null) {
                    conversation.setMessage(message);
                    notifyChangedListeners(ChangeEventListener.EventType.ADDED, index);

                }

            } else {
                if (isKeyAtIndex(key, index)) {
                    mConversations.remove(index);
                    notifyChangedListeners(ChangeEventListener.EventType.REMOVED, index);
                } else {
                    Log.w("ConversationsArray", "Key not found at ref: " + snapshot.getRef());
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            notifyCancelledListeners(databaseError);
        }
    }

}
