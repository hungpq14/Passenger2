package com.fit.uet.passengerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.AbstractItem;
import com.fit.uet.passengerapp.models.CenterItem;
import com.fit.uet.passengerapp.models.EdgeItem;

import java.util.List;

/**
 * Created by phamtruong on 3/10/17.
 */

public class SeatsAdapter extends SelectableAdapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AbstractItem> mItems;

    private OnSeatSelected mOnSeatSelected;

    public SeatsAdapter(Context context, List<AbstractItem> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    public void setmOnSeatSelected(OnSeatSelected mOnSeatSelected) {
        this.mOnSeatSelected = mOnSeatSelected;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        if (viewType == AbstractItem.TYPE_CENTER) {
            View itemView = mLayoutInflater.inflate(R.layout.item_seat, parent, false);
            return new CenterViewHolder(itemView);
        } else if (viewType == AbstractItem.TYPE_EDGE) {
            View itemView = mLayoutInflater.inflate(R.layout.item_seat, parent, false);
            return new EdgeViewHolder(itemView);
        } else if (viewType == AbstractItem.TYPE_ORDERED) {
            View itemView = mLayoutInflater.inflate(R.layout.item_seat_ordered, parent, false);
            return new EdgeViewHolder(itemView);
        } else {
            View itemView = new View(context);
            return new EmptyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = mItems.get(position).getType();
        if (type == AbstractItem.TYPE_CENTER) {
            final CenterItem item = (CenterItem) mItems.get(position);
            CenterViewHolder viewHolderolder = (CenterViewHolder) holder;

            viewHolderolder.imgSeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSelection(position);
                    if (mOnSeatSelected != null)
                        mOnSeatSelected.onSeatSelected(getSelectedItemCount());
                }
            });

            viewHolderolder.imgSeatSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

        } else if (type == AbstractItem.TYPE_EDGE) {
            final EdgeItem item = (EdgeItem) mItems.get(position);
            EdgeViewHolder viewHolder = (EdgeViewHolder) holder;

            viewHolder.imgSeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSelection(position);
                    if (mOnSeatSelected != null)
                        mOnSeatSelected.onSeatSelected(getSelectedItemCount());
                }
            });

            viewHolder.imgSeatSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }


    private static class EdgeViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeat;
        ImageView imgSeatSelected;


        public EdgeViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);
        }

    }

    private static class CenterViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeat;
        private final ImageView imgSeatSelected;

        public CenterViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);
        }

    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

    }

    public interface OnSeatSelected {
        void onSeatSelected(int count);
    }
}
