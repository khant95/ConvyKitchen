package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class GetFoodOrderActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference OrderRef;
    FirebaseRecyclerAdapter<ViewOrder, ViewOrderViewHolder> adapter;
    FirebaseRecyclerOptions<ViewOrder> options;
    RecyclerView recyclerView;


    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_food_order);

        mToolBar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        OrderRef = FirebaseDatabase.getInstance().getReference().child("Order");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        LoadOrders();

    }

    private void LoadOrders() {
        options = new FirebaseRecyclerOptions.Builder<ViewOrder>().setQuery(OrderRef, ViewOrder.class).build();
        adapter =new FirebaseRecyclerAdapter<ViewOrder, ViewOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewOrderViewHolder holder, int position, @NonNull ViewOrder model) {
                if (mUser.getUid().equals(model.getResKey()))
                {
                    Picasso.get().load(model.getProfileImage()).into(holder.profileImageView);
                    holder.items.setText(model.getItems());
                    holder.username.setText(model.getUserName());
                    holder.address.setText(model.getCustomerAddress());
                    holder.price.setText(model.getMenuPrice());
                    holder.date.setText(model.getDate());
                    holder.name.setText(model.getMenuName());

                }
                else
                {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setVisibility(View.INVISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }

            @NonNull
            @Override
            public ViewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_got_food_order,parent,false);
                return new ViewOrderViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }
}