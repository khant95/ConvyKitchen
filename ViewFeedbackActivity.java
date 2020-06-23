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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ViewFeedbackActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference feedbackRef;
    FirebaseRecyclerAdapter<ViewFeedBack, ViewFeedbackViewHolder> adapter;
    FirebaseRecyclerOptions<ViewFeedBack> options;
    RecyclerView recyclerView;


    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);


        mToolBar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        feedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback").child(mUser.getUid());


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);


        LoadFeedback();


    }

    private void LoadFeedback() {

        options = new FirebaseRecyclerOptions.Builder<ViewFeedBack>().setQuery(feedbackRef, ViewFeedBack.class).build();
        adapter = new FirebaseRecyclerAdapter<ViewFeedBack, ViewFeedbackViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewFeedbackViewHolder holder, int position, @NonNull ViewFeedBack model) {

                Picasso.get().load(model.getProfileImage()).into(holder.profileImage);
                holder.date.setText(model.getDate());
                holder.feedback.setText(model.getFeedback());
                holder.name.setText(model.getMenuName());
                holder.username.setText(model.getFullName());
            }

            @NonNull
            @Override
            public ViewFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_view_feedback, parent, false);

                return new ViewFeedbackViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

}