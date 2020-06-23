package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddMenuActivity extends AppCompatActivity {

    Toolbar mToolBar;
    TextInputLayout MenuName, MenuPrice, MenuDescription, MenuCategory;
    ImageView adminImageFood;
    TextView adminImageFoodText;
    Button adminBtnAddMenu;
    public static final int IMAGE_REQUEST_CODE_POST = 103;
    Uri uri;
    ProgressDialog mLoadingBar;

    DatabaseReference mFoodAddRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference mFoodImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        mToolBar = findViewById(R.id.addMenuAppbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("ADD Menu Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        //varibale initlization
        MenuName = findViewById(R.id.MenuName);
        MenuPrice = findViewById(R.id.MenuPrice);
        MenuDescription = findViewById(R.id.MenuMenuDesciption);
        MenuCategory = findViewById(R.id.MenuMenuCategory);

        adminImageFoodText = findViewById(R.id.admin_image_food_text);
        adminImageFood = findViewById(R.id.admin_image_food);
        adminBtnAddMenu = findViewById(R.id.btnAddMenu);


        mLoadingBar = new ProgressDialog(this);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFoodAddRef = FirebaseDatabase.getInstance().getReference().child("Food");
        mFoodImageRef = FirebaseStorage.getInstance().getReference().child("FoodImage");

        adminBtnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADDFood();
            }
        });
        adminImageFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST_CODE_POST);
            }
        });
        adminImageFoodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST_CODE_POST);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE_POST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            adminImageFood.setImageURI(uri);
        }
    }

    private void ADDFood() {

        final String name = MenuName.getEditText().getText().toString();
        final String price = MenuPrice.getEditText().getText().toString();
        final String description = MenuDescription.getEditText().getText().toString();
        final String category = MenuCategory.getEditText().getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            showError(MenuName, "Menu must be greater than 3 latter");
        } else if (price.isEmpty() || price.equals(0)) {
            showError(MenuPrice, "Add proper price ");
        } else if (description.isEmpty() || description.length() < 10) {
            showError(MenuDescription, "Description must greater than 10 latter");
        } else if (category.isEmpty() || category.length() < 3) {
            showError(MenuCategory, "Category must greater than 3 latter");
        } else if (uri == null) {
            adminImageFoodText.setText("Please Select your Image");
        } else {
            mLoadingBar.setTitle("Setup You Profile");
            mLoadingBar.setMessage("Please wait,While Saving your Menu...");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            final String key = mFoodAddRef.push().getKey();
            mFoodImageRef.child(mUser.getUid()).child(key).child(category).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mFoodImageRef.child(mUser.getUid()).child(key).child(category).getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final HashMap hashMap = new HashMap();
                                        hashMap.put("menuName", name);
                                        hashMap.put("MenuPrice", price);
                                        hashMap.put("MenuDescription", description);
                                        hashMap.put("MenuCategory", category);
                                        hashMap.put("MenuImageUri", uri.toString());

                                        mFoodAddRef.child(mUser.getUid()).child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AddMenuActivity.this, "Menu Added", Toast.LENGTH_SHORT).show();
                                                    MenuName.getEditText().setText("");
                                                    MenuDescription.getEditText().setText("");
                                                    MenuPrice.getEditText().setText("");
                                                    MenuCategory.getEditText().setText("");
                                                    adminImageFood.setImageResource(R.drawable.ic_add_image);
                                                    mLoadingBar.dismiss();
                                                } else {
                                                    mLoadingBar.dismiss();
                                                    Toast.makeText(AddMenuActivity.this, "Something going Wrong", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddMenuActivity.this, "Try Again" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mLoadingBar.dismiss();
                        Toast.makeText(AddMenuActivity.this, "Something going Wrong Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AddMenuActivity.this, AdminActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}