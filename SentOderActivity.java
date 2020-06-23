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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SentOderActivity extends AppCompatActivity {

    DatabaseReference RecentOrder;
    FirebaseRecyclerAdapter<SentOrder, SentOrderVIewHolder> adapter;
    FirebaseRecyclerOptions<SentOrder> options;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_oder);

        mToolBar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Your Recent Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        RecentOrder = FirebaseDatabase.getInstance().getReference().child("Order");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        LoadOrder();
    }

    private void LoadOrder() {

        options = new FirebaseRecyclerOptions.Builder<SentOrder>().setQuery(RecentOrder, SentOrder.class).build();
        adapter = new FirebaseRecyclerAdapter<SentOrder, SentOrderVIewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SentOrderVIewHolder holder, final int position, @NonNull final SentOrder model) {

                if (mUser.getUid().equals(model.getUserID()) ) {

                    Picasso.get().load(model.getMenuImageUri()).into(holder.image);
                    holder.totalPrice.setText("Total Price: " + model.getTotalPrice() + "$");
                    holder.price.setText("Price/Items: " + model.getMenuPrice() + "$");
                    holder.itmes.setText("Items: " + model.getItems());
                    holder.name.setText(model.getMenuName());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(SentOderActivity.this, AddFeedBackActivity.class);
                            intent.putExtra("name", model.getMenuName());
                            intent.putExtra("username", model.getUserName());
                            intent.putExtra("MenuimageUrl", model.getMenuImageUri());
                            intent.putExtra("price", model.getMenuPrice());
                            intent.putExtra("key", getRef(position).getKey());
                            intent.putExtra("date", model.getDate());
                            intent.putExtra("resKey", model.getResKey());
                            intent.putExtra("foodKey", model.getFoodKey());
                            intent.putExtra("profileImage", model.getProfileImage());
                            startActivity(intent);
                        }


                    });
                }
                else
                {
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setVisibility(View.INVISIBLE);
                }
            }

            @NonNull
            @Override
            public SentOrderVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_send_order, parent, false);

                return new SentOrderVIewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }
}