package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.ConversationsAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.Conversation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 10/03/2017.
 */

public class ConversationActivity extends BaseToolBarActivity implements ConversationsAdapter.OnItemClickListener {
    @BindView(R.id.recycler)
    RecyclerView mList;
    private ConversationsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        String self = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationsAdapter(mRef, mRef.child(DB.CHAT).child(self), self);
        mAdapter.setListener(this);
        mList.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conversation_list;
    }

    @Override
    protected String getToolbarText() {
        return getString(R.string.conversation_title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public void onItemClick(Conversation conversation) {
        Intent intent = new Intent(this,MessageListActivity.class);
        intent.putExtra(MessageListActivity.KEY_DESTINATION,conversation.getUser().getUid());
        startActivity(intent);
    }
}
