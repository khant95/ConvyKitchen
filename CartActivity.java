package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    DatabaseReference FoodRef, cartRef, UserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    //varibale
    ImageView imageView;
    TextView name, price, description, counter;
    Button btnNegetive, btnPositive, btnAddToCard;

    //items for cart
    int c = 1;
    HashMap hashMap = new HashMap();

    //data storeed for menu as global
    String MenuCategory;
    String MenuDescription;
    String MenuImageUri;
    String MenuPrice;
    String menuName;


    String userAddress;
    String userName;
    String profileImage;
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mToolBar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Order Food");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String Foodkey = getIntent().getStringExtra("foodKey");
        final String Reskey = getIntent().getStringExtra("resKey");
        Toast.makeText(this, Foodkey, Toast.LENGTH_SHORT).show();

        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        btnNegetive = findViewById(R.id.btnNegetive);
        btnPositive = findViewById(R.id.btnPositive);
        btnAddToCard = findViewById(R.id.btnAddToCart);
        counter = findViewById(R.id.counter);


        counter.setText("" + c);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c++;
                counter.setText("" + c);
            }
        });
        btnNegetive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c > 1) {
                    c--;
                }
                counter.setText("" + c);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(mUser.getUid());
        FoodRef = FirebaseDatabase.getInstance().getReference().child("Food").child(Reskey).child(Foodkey);
        cartRef = FirebaseDatabase.getInstance().getReference().child("Order");


        LoadFood();
        btnAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String strDate= formatter.format(date);

                hashMap.put("MenuCategory", MenuCategory);
                hashMap.put("MenuDescription", MenuCategory);
                hashMap.put("MenuImageUri", MenuImageUri);
                hashMap.put("MenuPrice", MenuPrice);
                hashMap.put("menuName", menuName);
                hashMap.put("ResKey", Reskey);
                hashMap.put("FoodKey", Foodkey);
                hashMap.put("userName", userName);
                hashMap.put("userID", mUser.getUid());
                hashMap.put("items", "" + c);
                hashMap.put("totalPrice", "" + c*Integer.parseInt(MenuPrice));
                hashMap.put("date", "" + strDate);
                hashMap.put("profileImage",profileImage);
                hashMap.put("customerAddress",userAddress);

                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Order now");
                builder.setMessage(userAddress);
                builder.setPositiveButton("Conform", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pushKey=cartRef.push().getKey().toString();
                        cartRef.child(pushKey).setValue(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CartActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CartActivity.this,CustomerActivity.class));
                                } else {
                                    Toast.makeText(CartActivity.this, "Try Again,Something Going Wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CartActivity.this, "You have Cancel Order", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();


            }
        });
    }

    private void LoadFood() {
        FoodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //get data
                    MenuCategory = dataSnapshot.child("MenuCategory").getValue().toString();
                    MenuDescription = dataSnapshot.child("MenuDescription").getValue().toString();
                    MenuImageUri = dataSnapshot.child("MenuImageUri").getValue().toString();
                    MenuPrice = dataSnapshot.child("MenuPrice").getValue().toString();
                    menuName = dataSnapshot.child("menuName").getValue().toString();

                    //assign data
                    Picasso.get().load(MenuImageUri).placeholder(R.drawable.loader).into(imageView);
                    price.setText("Price :" + MenuPrice);
                    name.setText(menuName);
                    description.setText(MenuDescription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    userAddress = "We will send food parcels at "+ dataSnapshot.child("streetAddress").getValue().toString() + "," + dataSnapshot.child("city").getValue().toString() +
                            "," + dataSnapshot.child("country").getValue().toString();
                    userName=dataSnapshot.child("fullName").getValue().toString();
                    profileImage = dataSnapshot.child("profileImage").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}