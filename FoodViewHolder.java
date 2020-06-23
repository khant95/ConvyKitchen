package com.example.convykitchen;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.convykitchen.R;

class FoodViewHolder extends RecyclerView.ViewHolder {
    TextView menuName,MenuPrice;
    ImageView imageView;



    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);
        menuName=itemView.findViewById(R.id.menuName);
        MenuPrice=itemView.findViewById(R.id.menuPrice);


    }
}
