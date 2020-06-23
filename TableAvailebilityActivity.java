package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TableAvailebilityActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<TableItems,TableViewHolder> adapter;
    FirebaseRecyclerOptions<TableItems> options;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_availebility);

      final  String key=getIntent().getStringExtra("key");
        mToolBar=findViewById(R.id.tabelStatus);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Reserve Table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.tableRecyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference().child("Tables").child(key);

        LoadData();
    }

    private void LoadData() {
        options=new FirebaseRecyclerOptions.Builder<TableItems>().setQuery(mRef,TableItems.class).build();
        adapter=new FirebaseRecyclerAdapter<TableItems, TableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TableViewHolder holder, final int position, @NonNull TableItems model) {
                final String key=getRef(position).getKey();

                final boolean status=model.isStatus();
                if (status)
                {
                    holder.status.setVisibility(View.GONE);
                    holder.view.setEnabled(true);
                }
                else
                {
                    holder.status.setVisibility(View.VISIBLE);
                    holder.view.setEnabled(false);
                }
                holder.tableNumber.setText("Table No."+getSnapshots().getSnapshot(position).getKey());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangeStatus(key,status);
                    }
                });

            }

            @NonNull
            @Override
            public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_table,parent,false);

                return new TableViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void ChangeStatus(String key, boolean status) {
        if (status)
        {

            mRef.child(key).child("status").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(TableAvailebilityActivity.this, "Reserved!", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        else
        {
            mRef.child(key).child("status").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(TableAvailebilityActivity.this, "Available!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}