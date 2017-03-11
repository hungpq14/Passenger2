package com.fit.uet.passengerapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.CoachHost;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bien-kun on 11/03/2017.
 */

public class FavoriteCoachHostAdapter extends FirebaseIndexRecyclerAdapter<CoachHost, FavoriteCoachHostAdapter.CoachHoastHolder> {
    public FavoriteCoachHostAdapter(Class<CoachHost> modelClass, @LayoutRes int modelLayout, Class<CoachHoastHolder> viewHolderClass, Query keyRef, Query dataRef) {
        super(modelClass, modelLayout, viewHolderClass, keyRef, dataRef);
    }

    @Override
    protected void populateViewHolder(CoachHoastHolder viewHolder, CoachHost model, int position) {
        viewHolder.bind(model);
    }

    public static class CoachHoastHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.company_name)
        TextView mHostView;
        @BindView(R.id.rating)
        RatingBar mRattingBar;

        private Context mContext;


        public CoachHoastHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }


        public void bind(CoachHost coachHost) {
            mHostView.setText(coachHost.name);
            mRattingBar.setRating(coachHost.star);

        }

    }
}
