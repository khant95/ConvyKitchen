package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference mAdminRef,RecyclerViewRef;
    Toolbar mToolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView profileHeaderImage;
    ProgressBar progressBar;
    TextView username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        //firebase initilization
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAdminRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        RecyclerViewRef = FirebaseDatabase.getInstance().getReference().child("Food").child(mUser.getUid());

        CheckUserExistance();

        mToolBar = findViewById(R.id.myToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Admin Dashbord");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);


        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        progressBar = findViewById(R.id.progressBar);




        View v = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileHeaderImage = v.findViewById(R.id.profile_image_haeder);
        username = v.findViewById(R.id.profile_username_header);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.addMenu:
                        startActivity(new Intent(AdminActivity.this, AddMenuActivity.class));
                        break;
                    case R.id.addTable:
                        startActivity(new Intent(AdminActivity.this, AddTableActivity.class));
                        break;
                    case R.id.tabelStatus:
                        startActivity(new Intent(AdminActivity.this, TableStatusActivity.class));
                        break;
                    case R.id.foodOrder:
                        startActivity(new Intent(AdminActivity.this, GetFoodOrderActivity.class));
                        break;

                    case R.id.viewfeedback:
                        startActivity(new Intent(AdminActivity.this, ViewFeedbackActivity.class));
                        break;
                    case R.id.logout:
                        mAuth.signOut();
                        sendToUserSelection();
                        break;
                }
                return true;
            }
        });
    }

    private void LoadData() {

    }

    private void CheckUserExistance() {
        if (mUser == null) {
            sendAdminToAuthActivity();
        } else {
            mAdminRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        sendAdminToAuthActivity();
                    } else {
                        String profileImage = dataSnapshot.child("logoImageUrl").getValue().toString();
                        String fullName = dataSnapshot.child("username").getValue().toString();
                        Picasso.get().load(profileImage).placeholder(R.drawable.loader).into(profileHeaderImage);
                        username.setText(fullName);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private void sendAdminToAuthActivity() {
        Intent intent = new Intent(this, AdminAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mUser==null)
//        {
//            SendToAdminAuth();
//        }
//        else
//        {
//            mAdminRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (!dataSnapshot.exists())
//                    {
//                        SendToAdminAuth();
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//    }

    private void SendToAdminAuth() {
        Intent intent = new Intent(this, AdminAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendToUserSelection() {
        Intent intent = new Intent(this, UserSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
