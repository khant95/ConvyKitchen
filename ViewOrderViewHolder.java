package com.example.convykitchen;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

class ViewOrderViewHolder  extends RecyclerView.ViewHolder {
    CircleImageView profileImageView;
    TextView username,date,name,items,price,address;
    public ViewOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImageView=itemView.findViewById(R.id.circleImageView);
        date=itemView.findViewById(R.id.date);
        price=itemView.findViewById(R.id.price);
        address=itemView.findViewById(R.id.address);
        name=itemView.findViewById(R.id.name);
        username=itemView.findViewById(R.id.username);
        items=itemView.findViewById(R.id.items);

    }
}
