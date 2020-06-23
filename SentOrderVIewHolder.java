package com.example.convykitchen;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class SentOrderVIewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView name,date,price,itmes,totalPrice;
    public SentOrderVIewHolder(@NonNull View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.image);
        name=itemView.findViewById(R.id.name);
        date=itemView.findViewById(R.id.date);
        itmes=itemView.findViewById(R.id.totalItems);
        price=itemView.findViewById(R.id.pricePerItem);
        totalPrice=itemView.findViewById(R.id.totalPrice);
    }
}
