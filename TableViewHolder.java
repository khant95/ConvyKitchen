package com.example.convykitchen;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TableViewHolder extends RecyclerView.ViewHolder {
    ImageView statusImageView,status;
    View view;
    TextView tableNumber;
    public TableViewHolder(@NonNull View itemView) {
        super(itemView);
        statusImageView=itemView.findViewById(R.id.tabelStatus);
        status=itemView.findViewById(R.id.status);
        tableNumber=itemView.findViewById(R.id.tableNumber);
        view=itemView;

    }
}
