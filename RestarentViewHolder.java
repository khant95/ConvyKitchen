package com.example.convykitchen;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;


class RestarentViewHolder extends RecyclerView.ViewHolder {
    CircleImageView logo;
    TextView restuarentname,address,city,country;
    Button btnOrderFood,btnTable;
    View v;

    public RestarentViewHolder(@NonNull View itemView) {
        super(itemView);
        logo=itemView.findViewById(R.id.logo);
        restuarentname=itemView.findViewById(R.id.restuarentname);
        address=itemView.findViewById(R.id.address);
        city=itemView.findViewById(R.id.city);
        country=itemView.findViewById(R.id.country);
        v=itemView;
        btnOrderFood=itemView.findViewById(R.id.btnFood);
        btnTable=itemView.findViewById(R.id.btnTable);


    }
}
