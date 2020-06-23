package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView image;
    TextView fullname,phone,city,country,address;
    DatabaseReference  mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolBar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image=findViewById(R.id.image);
        fullname=findViewById(R.id.fullname);
        city=findViewById(R.id.city);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        country=findViewById(R.id.country);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        mRef= FirebaseDatabase.getInstance().getReference().child("Customer").child(mUser.getUid());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String profileImageT=dataSnapshot.child("profileImage").getValue().toString();
                    String fullnameT=dataSnapshot.child("fullName").getValue().toString();
                    String cityT=dataSnapshot.child("city").getValue().toString();
                    String addressT=dataSnapshot.child("streetAddress").getValue().toString();
                    String countryT=dataSnapshot.child("country").getValue().toString();
                    String phoneT=dataSnapshot.child("phone").getValue().toString();

                    Picasso.get().load(profileImageT).into(image);
                    fullname.setText(fullnameT);
                    city.setText(cityT);
                    country.setText(countryT);
                    address.setText(addressT);
                    phone.setText(phoneT);

                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}