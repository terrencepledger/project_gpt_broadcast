package org.gptchurch.broadcast.ui.broadcastPage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.gptchurch.broadcast.R;
import org.gptchurch.broadcast.model.Sermon;
import org.gptchurch.broadcast.model.SermonLog;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class BroadcastLogRecyclerAdapter extends
        RecyclerView.Adapter<BroadcastLogRecyclerAdapter.ViewHolder> {

    private ArrayList<SermonLog> logs;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView logName;
        private final TextView logSentDate;
        private final TextView logCDDate;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            logName = view.findViewById(R.id.logItemName);
            logSentDate = view.findViewById(R.id.logItemSentDate);
            logCDDate = view.findViewById(R.id.logItemCDDate);
        }

        public TextView getLogNameTextView() {
            return logName;
        }

        public TextView getLogSentDateTextView() {
            return logSentDate;
        }

        public TextView getLogCDDateTextView() {
            return logCDDate;
        }


    }

    public BroadcastLogRecyclerAdapter() {
        this.logs = new ArrayList<>();
    }

    public void add(SermonLog log) {
        logs.add(log);
        notifyItemInserted(logs.size() - 1);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_broadcast_log, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getLogNameTextView().setText(logs.get(position).sermon.title);
        viewHolder.getLogSentDateTextView().setText(logs.get(position).getPrettyDate());
        viewHolder.getLogCDDateTextView().setText(logs.get(position).sermon.getPrettyDate());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return logs.size();
    }

}