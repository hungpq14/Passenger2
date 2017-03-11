package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.CartAdapter;
import com.fit.uet.passengerapp.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends BaseToolBarActivity {

    @BindView(R.id.rv_cart)
    RecyclerView rv_cart;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        databaseReference.child(Ticket.CHILD_TICKET).orderByChild("user_id").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ticket> tickets = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        tickets.add(snapshot.getValue(Ticket.class));
                }

                initView(databaseReference, tickets);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Cancel", databaseError.getMessage());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart;
    }

    @Override
    protected String getToolbarText() {
        return "Cart";
    }

    private void initView(DatabaseReference databaseReference, final List<Ticket> tickets) {
        CartAdapter adapter = new CartAdapter(this, tickets, databaseReference);
        rv_cart.setLayoutManager(new LinearLayoutManager(this));
        rv_cart.setAdapter(adapter);

        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CartActivity.this, TicketActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, tickets.get(position).uid);
                startActivity(intent);
            }
        });
    }
}
