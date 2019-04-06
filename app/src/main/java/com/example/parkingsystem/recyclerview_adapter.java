package com.example.parkingsystem;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class recyclerview_adapter extends RecyclerView.Adapter<recyclerview_adapter.ViewHolder> {
    ArrayList<recyclerview_item> mitems;

    public recyclerview_adapter(ArrayList<recyclerview_item> mitems, int layout, spot_check spot_check) {
        this.mitems = mitems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pose) {
        holder.mspot_name.setText(mitems.get(pose).getSpot_name());
        if(mitems.get(pose).isUse()) {
            holder.mspot_name.setBackgroundColor(Color.BLUE);
        } else {

            holder.mspot_name.setBackgroundColor(Color.WHITE);
        }//
    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mspot_name;
        public ViewHolder(View v) {
            super(v);
            mspot_name = (TextView) v.findViewById(R.id.spot_name);
        }
    }
}
