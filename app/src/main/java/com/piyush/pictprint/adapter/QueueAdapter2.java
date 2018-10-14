package com.piyush.pictprint.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piyush.pictprint.R;
import com.piyush.pictprint.Utils.Utils;
import com.piyush.pictprint.model.Document;

import java.util.ArrayList;
import java.util.List;

public class QueueAdapter2 extends RecyclerView.Adapter<QueueAdapter2.QueueItemViewHolder2> {
    private List<Document> documents;

    public QueueAdapter2() {
        documents = new ArrayList<>();
    }

    public void setDoucments(List<Document> doucments)
    {
        this.documents = doucments;
        notifyDataSetChanged();
    }

    public void addDocument(Document document)
    {
        documents.add(document);
        notifyItemInserted(documents.size());
    }

    public void addDocuments(List<Document> documents)
    {
        Log.d("QUEUEADAPTER",String.valueOf(documents.size()));
        this.documents=documents;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public QueueItemViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new QueueItemViewHolder2(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_queue,
                        viewGroup,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull QueueItemViewHolder2 queueItemViewHolder, int i) {
        Document document = documents.get(i);
        queueItemViewHolder.price.setText(String.format("₹ %s", String.valueOf(document.getPrice())));
        queueItemViewHolder.fileName.setText(document.getName());
        queueItemViewHolder.icon
                .setImageDrawable(Utils
                        .getIcon(document
                                        .getContentType(),
                                queueItemViewHolder.itemView.getContext()));
        String text = String.valueOf(document
                .getpages())+(document.getpages()==1?" page":" pages")
                +" • "+
                DateUtils.getRelativeTimeSpanString(document.getTime());
        queueItemViewHolder.count_and_time.setText(text);
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public void deletee(int position) {
        Log.d("QUEUEREMOVE",String.valueOf(position));
        Log.d("QUQUESIZE",String.valueOf(documents.size()));
        documents.remove(position);
        notifyItemRemoved(position);

    }

    class QueueItemViewHolder2 extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView fileName;
        TextView count_and_time;
        TextView price;

        QueueItemViewHolder2(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            fileName = itemView.findViewById(R.id.file_name);
            count_and_time = itemView.findViewById(R.id.count_and_time);
            price = itemView.findViewById(R.id.price);
        }
    }
}
