package com.piyush.pictprint.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piyush.pictprint.R;

public class TotalItemsAdapter extends RecyclerView.Adapter<TotalItemsAdapter.ItemViewHolder> {
    @NonNull
    @Override
    public TotalItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.total_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TotalItemsAdapter.ItemViewHolder itemViewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

