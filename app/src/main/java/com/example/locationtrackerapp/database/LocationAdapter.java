package com.example.locationtrackerapp.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.locationtrackerapp.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter
        extends RecyclerView.Adapter<LocationAdapter.VH> {

    private List<LocationEntity> list = new ArrayList<>();

    public void setData(List<LocationEntity> l) {
        list = l;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView t;

        VH(View v) {
            super(v);
            t = v.findViewById(R.id.txtLocation);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        LocationEntity e = list.get(position);
        holder.t.setText(
                "Lat: " + e.latitude +
                        "\nLng: " + e.longitude +
                        "\nTime: " + e.timestamp
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
