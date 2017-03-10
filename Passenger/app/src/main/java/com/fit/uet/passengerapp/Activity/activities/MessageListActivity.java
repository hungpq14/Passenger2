package com.fit.uet.passengerapp.Activity.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.MessageListAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.Message;
import com.fit.uet.passengerapp.models.User;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 10/03/2017.
 */

public class MessageListActivity extends BaseToolBarActivity implements ValueEventListener {
    public static final String KEY_DESTINATION = "destination";
    @BindView(R.id.recycler)
    RecyclerView mList;
    @BindView(R.id.btn_send)
    ImageButton mSendView;
    @BindView(R.id.edt_message)
    EditText mMessageView;
    private MessageListAdapter mAdapter;
    private DatabaseReference mRef;
    private String mSelf, mDestination;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mDestination = getIntent().getStringExtra(KEY_DESTINATION);
        mSelf = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.child(DB.USER).child(mDestination).addValueEventListener(this);

        mList.setLayoutManager(new LinearLayoutManager(this));
        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mMessageView.getText().toString();
                if (!TextUtils.isEmpty(msg)) {
                    Message message = new Message(mSelf, mDestination, msg, DateTimeUtils.getCurrentTimestamp());
                    submiMessage(message);
                    mMessageView.setText("");

                }
            }
        });


    }

    private void submiMessage(Message message) {
        mRef.child(DB.CHAT).child(mSelf).child(message.getDestination()).child(DB.MESSAGES).push().setValue(message);
        mRef.child(DB.CHAT).child(message.getDestination()).child(mSelf).child(DB.MESSAGES).push().setValue(message);
    }

    public void clear() {
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected String getToolbarText() {
        return getString(R.string.message_title);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (user != null && mAdapter == null) {
            setTitle(user.getName());
            mAdapter = new MessageListAdapter(mList,user, DB.getMessageForUserRef(mRef, mSelf, mDestination).orderByChild("timestamp"));
            mList.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        clear();
        super.onDestroy();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
