package com.brianvega.tourmalineserviceexample.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brianvega.tourmalineserviceexample.R;
import com.brianvega.tourmalineserviceexample.models.Message;

public class LogsViewHolder extends RecyclerView.ViewHolder {

    TextView message;

    public LogsViewHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.txt_message);
    }

    public void bind(Message m) {
        message.setText( m.message);
        message.setTextColor(m.color);
    }
}
