package com.piyush.pictprint.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piyush.pictprint.R;

import java.util.ArrayList;
import java.util.List;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder> {

    private List<Bitmap> list;

    public PreviewAdapter() {
        list = new ArrayList<>();
    }

    public void setList(List<Bitmap> list)
   {
       this.list=list;
       notifyDataSetChanged();
   }

    @NonNull
    @Override
    public PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PreviewViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_print_preview
                        ,viewGroup,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewViewHolder previewViewHolder, int i) {
        previewViewHolder.preview.setImageBitmap(list.get(i));
        previewViewHolder.number.setText(String.format("%s/%s",
                String.valueOf(i + 1),
                String.valueOf(list.size())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PreviewViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        ImageView preview;
        public PreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.page_no);
            preview = itemView.findViewById(R.id.preview_image);
        }
    }
}
