package com.fit.uet.passengerapp.Activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fit.uet.passengerapp.Activity.BaseActivity.BaseToolBarActivity;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.adapter.CartAdapter;
import com.fit.uet.passengerapp.database.DB;
import com.fit.uet.passengerapp.models.Comment;
import com.fit.uet.passengerapp.models.Ticket;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
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

public class CartActivity extends BaseToolBarActivity implements MaterialDialog.SingleButtonCallback {

    @BindView(R.id.rv_cart)
    RecyclerView rv_cart;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    FirebaseUser currentUser;

    private DatabaseReference databaseReference;
    private MaterialDialog mDialog;
    private EditText mEdtComment;
    private RatingBar mRating;

    private String mHostUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

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
        mDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_review, false)
                .positiveColor(ContextCompat.getColor(CartActivity.this, R.color.colorAccent))
                .onPositive(this)
                .positiveText(R.string.dialog_review_action_positive)
                .negativeText(R.string.dialog_review_action_negative)
                .title(R.string.dialog_review_title)
                .build();

        mEdtComment = (EditText) mDialog.getCustomView().findViewById(R.id.edt_comment);
        mRating = (RatingBar) mDialog.getCustomView().findViewById(R.id.rating_bar);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart;
    }

    @Override
    protected String getToolbarText() {
        return "Vé đã mua";
    }

    private void initView(final DatabaseReference databaseReference, final List<Ticket> tickets) {
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

            @Override
            public void onReviewClick(String uid) {
                //TODO: tim id nha xe dua vao id cua schedule
                if (uid == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Đang load dữ liệu", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                mHostUid = uid;
                mDialog.show();

            }

            @Override
            public void onFinishGetData() {
                show();
            }
        });
    }

    private void show() {
        progressBar.setVisibility(View.INVISIBLE);
        rv_cart.setVisibility(View.VISIBLE);
    }

    private void hide() {
        progressBar.setVisibility(View.VISIBLE);
        rv_cart.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        Comment comment = new Comment();
        comment.content = mEdtComment.getText().toString();
        comment.star = mRating.getRating();
        comment.timestamp = DateTimeUtils.getCurrentTimestamp();
        comment.coachHostUid = mHostUid;
        comment.name =  currentUser.getDisplayName();
        comment.userUid = currentUser.getUid();

        databaseReference.child(DB.COMMENTS).push().setValue(comment);
        mDialog.dismiss();
        Snackbar.make(findViewById(android.R.id.content), "Cảm ơn vì sự phản hồi của bạn", Snackbar.LENGTH_SHORT).show();
    }
}
