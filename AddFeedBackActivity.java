package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AddFeedBackActivity extends AppCompatActivity {

    Toolbar mToolBar;
    DatabaseReference mFeedRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText input;
    ImageView btnSend,imageView;
    TextView name,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed_back);

        mToolBar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Add Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        input=findViewById(R.id.input);
        btnSend=findViewById(R.id.btnSend);
        imageView=findViewById(R.id.imageview);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);


        String key=getIntent().getStringExtra("key");

        final String nameT=getIntent().getStringExtra("name");
        final String foodImageUrlT=getIntent().getStringExtra("image");
        final String priceT=getIntent().getStringExtra("price");
        final String userNameT=getIntent().getStringExtra("username");
        final String dateT=getIntent().getStringExtra("date");
        final String resKeyT=getIntent().getStringExtra("resKey");
        final String foodKeyT=getIntent().getStringExtra("foodKey");
        final String profileImageT=getIntent().getStringExtra("profileImage");

        name.setText(nameT);
        price.setText("Price :"+priceT+"$");
        Picasso.get().load(foodImageUrlT).into(imageView);


        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mFeedRef= FirebaseDatabase.getInstance().getReference().child("Feedback").child(resKeyT);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=mFeedRef.push().getKey().toString();
                String feedback=input.getText().toString();

                if (feedback!=null)
                {
                    final HashMap hashMap=new HashMap();
                    hashMap.put("menuName",nameT);
                    hashMap.put("FoodKey",foodKeyT);
                    hashMap.put("ResKey",resKeyT);
                    hashMap.put("MenuImageUri",foodImageUrlT);
                    hashMap.put("fullName",userNameT);
                    hashMap.put("profileImage",profileImageT);
                    hashMap.put("MenuPrice",priceT);
                    hashMap.put("date",dateT);
                    hashMap.put("feedback",feedback);

                    mFeedRef.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                startActivity(new Intent(AddFeedBackActivity.this,CustomerActivity.class));
                                Toast.makeText(AddFeedBackActivity.this, "FeedBack Added", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(AddFeedBackActivity.this, "feed back not Added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(AddFeedBackActivity.this, "Add Some feed back", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}