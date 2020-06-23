package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSelectionActivity extends AppCompatActivity {

    private Button btnAdmin, btnCustomer;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mAdminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        //     Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//      initlilization
        btnAdmin = findViewById(R.id.btnAdmin);
        btnCustomer = findViewById(R.id.btnCustomer);


        //firebaseUser
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAdminRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        if (mUser != null) {
            //problem here
            if (!mUser.getPhoneNumber().trim().equals("")) {
                //change it latter
                Intent intent1 = new Intent(UserSelectionActivity.this, CustomerActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            } else if (mUser.getEmail().contains("@")) {
                startActivity(new Intent(UserSelectionActivity.this, AdminAuthActivity.class));
                finish();
            }

        } else {
            Toast.makeText(this, "Register OR login", Toast.LENGTH_SHORT).show();
        }


        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSelectionActivity.this, AdminAuthActivity.class));
            }
        });
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSelectionActivity.this, CustomerAuthActivity.class));
            }
        });

    }

}
