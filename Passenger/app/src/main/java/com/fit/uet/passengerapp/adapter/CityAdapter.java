package com.fit.uet.passengerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fit.uet.passengerapp.ListenerEvent.EventPickLocation;
import com.fit.uet.passengerapp.R;
import com.fit.uet.passengerapp.models.City;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by phamh_000 on 11/03/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private ArrayList<City> listCity;

    public CityAdapter(ArrayList<City> listCity) {
        this.listCity = listCity;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_city, parent, false);
        return new CityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        City city = listCity.get(position);
        holder.bind(city, position);
    }

    @Override
    public int getItemCount() {
        return listCity.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCityName;
        private View itemView;

        public CityViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtCityName = (TextView) itemView.findViewById(R.id.txt_city);
        }

        public void bind(City city, final int position) {
            txtCityName.setText(city.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventPickLocation(position));
                }
            });
        }
    }
}
