package com.fit.uet.passengerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.Comment;
import com.fit.uet.passengerapp.utils.DateTimeUtils;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 11/03/2017.
 */

public class CommentAdapter extends FirebaseRecyclerAdapter<Comment, CommentAdapter.CommentHolder> {

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public CommentAdapter(Class<Comment> modelClass, int modelLayout, Class<CommentHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
        viewHolder.bind(model);
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rating)
        RatingBar mRating;
        @BindView(R.id.comment)
        TextView mComment;
        @BindView(R.id.time)
        TextView mDate;

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Comment comment) {
            mRating.setRating(comment.star);
            mComment.setText(comment.content);
            mDate.setText(DateTimeUtils.getLocalTime(comment.timestamp));
        }
    }
}
