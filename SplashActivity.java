package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView logoText;
    private Animation logoAnim, logoTextAnim;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mAdminRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initlization
        logo = findViewById(R.id.logo);
        logoText = findViewById(R.id.logoText);
        logoAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.logo_anim);
        logoTextAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.logo_text_anim);


        //Assign Animation
        logo.setAnimation(logoAnim);
        logoText.setAnimation(logoTextAnim);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAdminRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        //Redirect to next Activity
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,UserSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable, 5000);
//        if (mUser != null) {
//            if (mUser != null) {
//                mAdminRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(SplashActivity.this, AdminAuthActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//        }
    }
}
