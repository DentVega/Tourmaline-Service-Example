package com.brianvega.tourmalineserviceexample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brianvega.tourmalineserviceexample.R;
import com.brianvega.tourmalineserviceexample.models.Message;
import java.util.List;

public class LogsRecyclerAdapter extends RecyclerView.Adapter<LogsViewHolder> {

    public List<Message> message;

    public LogsRecyclerAdapter(List<Message> message) {
        this.message = message;
    }

    @NonNull
    @Override
    public LogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_log, parent, false);
        LogsViewHolder viewHolder = new LogsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LogsViewHolder holder, int position) {
        holder.bind(message.get(position));
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

}
