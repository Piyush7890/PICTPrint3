package com.piyush.pictprint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piyush.pictprint.model.JobStatus;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
    private int[] colors;
    public LogAdapter(Context context) {
        colors =context.getResources().getIntArray(R.array.statusColors);
        jobs = new ArrayList<>();
    }

    private List<JobStatus> jobs;

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LogViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_log, viewGroup, false));
    }

    public void submitList(List<JobStatus> jobs)
    {
        this.jobs = jobs;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder logViewHolder, int i) {
        JobStatus status = jobs.get(i);

        logViewHolder.title.setText(status.getTitle());
        logViewHolder.timeAndPages.setText(new StringBuilder()
                .append(String
                        .valueOf(status
                                .getNumberOfPages())).append(" pages")
                .append(" • ").append(DateUtils.getRelativeTimeSpanString(Long.parseLong(status.getCreateTime())))
                .toString());

        ((GradientDrawable) logViewHolder
                .status
                .getCompoundDrawablesRelative()[0])
                .setColor(getCategoryColor(status.getStatus()));
        logViewHolder.status.setText(status.getStatus());
    }

    private int getCategoryColor(String status) {
         switch (status)
            {
                case "FAILED":
                    return colors[0];
                case "QUEUED":
                    return colors[1];

                case "IN_PROGRESS":

                case "PRINTED":
                    return colors[2];
            }
            return Color.WHITE;
    }


    @Override
    public int getItemCount() {
        return jobs.size();
    }
}

class LogViewHolder extends RecyclerView.ViewHolder {


    TextView title, status, timeAndPages;
    public LogViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.file_name);
        status = itemView.findViewById(R.id.status);
        timeAndPages = itemView.findViewById(R.id.count_and_time);

    }
}
