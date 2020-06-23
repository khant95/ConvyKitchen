package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class FoodActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference FoodRef;
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    FirebaseRecyclerOptions<Food> options;
    RecyclerView recyclerView;
    EditText Customer_input_search;

    Toolbar mToolBar;
    ProgressBar progressBar;
    TextView username;

     String Restkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        mToolBar=findViewById(R.id.customer_app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Select Food");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         Restkey=getIntent().getStringExtra("key");
        //firebase
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        FoodRef= FirebaseDatabase.getInstance().getReference().child("Food").child(Restkey);
        Customer_input_search=findViewById(R.id.Customer_input_search);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);


        LoadFood("");
        Customer_input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    LoadFood(s.toString());
                }
                else
                {
                    LoadFood("");
                }
            }
        });
    }


    private void LoadFood(String s) {


        Query query=FoodRef.orderByChild("menuName").startAt(s).endAt(s+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<Food>().setQuery(query,Food.class).build();
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                final String key=getRef(position).getKey();
                Picasso.get().load(model.getMenuImageUri()).placeholder(R.drawable.loader).into(holder.imageView);
                holder.menuName.setText(model.getMenuName());
                holder.MenuPrice.setText("Price: "+model.getMenuPrice()+"$");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(FoodActivity.this, CartActivity.class);
                        intent.putExtra("foodKey",key);
                        intent.putExtra("resKey",Restkey);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_item,parent,false);
                return new FoodViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }
}