package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class AddTableActivity extends AppCompatActivity {


    Toolbar mToolBar;
    TextInputLayout AdminInputTables;
    Spinner spinner1;
    Button adminBtnAddTables;
    ProgressDialog mLoadingBar;

    DatabaseReference TablesRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    boolean status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);


        mToolBar = findViewById(R.id.addTableAppbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("ADD Tables");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


        mLoadingBar = new ProgressDialog(this);
        spinner1 = findViewById(R.id.spinner1);


        //varibale initilization
        AdminInputTables = findViewById(R.id.admin_inputTable);
        adminBtnAddTables = findViewById(R.id.admin_Btn_Add_table);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        TablesRef = FirebaseDatabase.getInstance().getReference().child("Tables");

        //spiner initilization
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    status = false;
                } else {
                    status = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        adminBtnAddTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADDTables();
            }
        });
    }


    private void ADDTables() {

         final String table = AdminInputTables.getEditText().getText().toString();

        if (table.isEmpty() || table.equals(0)) {
            showError(AdminInputTables, "Add Table Number");
        } else {
            mLoadingBar.setTitle("Adding You Tables");
            mLoadingBar.setMessage("Please wait,While Saving your Tables...");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            TablesRef.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(table)) {
                        mLoadingBar.dismiss();
                        Toast.makeText(AddTableActivity.this, "This Table Already Added,Add any Other!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        TablesRef.child(mUser.getUid()).child(table).child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful())
                               {
                                   mLoadingBar.dismiss();
                                   AdminInputTables.getEditText().setText("");
                                   Toast.makeText(AddTableActivity.this, "Table Added!", Toast.LENGTH_SHORT).show();
                               }
                               else
                               {
                                   mLoadingBar.dismiss();
                                   Toast.makeText(AddTableActivity.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                               }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddTableActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AddTableActivity.this, AdminActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}