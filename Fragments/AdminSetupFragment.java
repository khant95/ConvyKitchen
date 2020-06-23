package com.example.convykitchen.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.convykitchen.AdminAuthActivity;
import com.example.convykitchen.AdminActivity;
import com.example.convykitchen.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class AdminSetupFragment extends Fragment {
  Context ctx;
  FirebaseAuth mAuth;
  FirebaseUser mUser;
  DatabaseReference mAdminRef;
  TextInputLayout adminInputUserName,adminInputResturantName,
          adminInputStreatAddress,adminInputCity
          ,adminInputCountry;
  ImageView adminLogoResturant;
  TextView adminLogoTextResturant;
  Button adminBtnSetup;
  ProgressDialog mLoadingBar;
  Uri uri=null;
  public static final int IMAGE_REQUEST_CODE_POST=101;
  StorageReference mLogoRef;


    public AdminSetupFragment(Context  context) {
        ctx=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        //Variable initilization
        adminInputUserName=view.findViewById(R.id.admin_input_username);
        adminInputResturantName=view.findViewById(R.id.admin_input_Resturant);
        adminInputStreatAddress=view.findViewById(R.id.admin_input_Address);
        adminInputCity=view.findViewById(R.id.admin_input_City);
        adminInputCountry=view.findViewById(R.id.admin_input_Country);
        adminLogoResturant=view.findViewById(R.id.admin_logo_resturant);
        adminLogoTextResturant=view.findViewById(R.id.admin_logo_resturant_text);
        adminBtnSetup=view.findViewById(R.id.admin_btn_setup);


        //loadingbar
        mLoadingBar=new ProgressDialog(ctx);
        //firebase
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mAdminRef= FirebaseDatabase.getInstance().getReference().child("Admin");
        mLogoRef= FirebaseStorage.getInstance().getReference().child("Admin");

        adminBtnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptSetup();
            }
        });
        adminLogoResturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST_CODE_POST);
            }
        });
        adminLogoTextResturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_REQUEST_CODE_POST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST_CODE_POST && resultCode==RESULT_OK && data!=null)
        {
            uri=data.getData();
            adminLogoResturant.setImageURI(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUser==null)
        {
            AdminAuthActivity.fragmentManager.beginTransaction()
                    .replace(R.id.admin_container,new AdminRegisterFragment(ctx)).commit();
        }
        else {
            mAdminRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        SenduserToMainActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ctx, "Admin Setup Error"+databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void SenduserToMainActivity() {
        Intent intent=new Intent(ctx.getApplicationContext(), AdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void AttemptSetup() {

        final String username = adminInputUserName.getEditText().getText().toString();
        final String resturantName = adminInputResturantName.getEditText().getText().toString();
        final String streetAddress = adminInputStreatAddress.getEditText().getText().toString();
        final String city = adminInputCity.getEditText().getText().toString();
        final String country = adminInputCountry.getEditText().getText().toString();

        if (username.isEmpty() || username.length()<4) {
            showError(adminInputUserName, "Username is not Valid");
        }else if (resturantName.isEmpty() || resturantName.length()<4) {
            showError(adminInputResturantName, "Restuarent name mus be greater tha 4 latter");
        }else if (streetAddress.isEmpty() || streetAddress.length()<4) {
            showError(adminInputStreatAddress, "Street Address must Greater than 10 Latter");
        }else if (city.isEmpty() || city.length()<4) {
            showError(adminInputCity, "City must greater than 3 latter");
        }
        else if (country.isEmpty() || country.length()<3) {
            showError(adminInputCountry, "Country must greater than 3 latter");
       } else if (uri==null ) {
            adminLogoTextResturant.setText("Please Select your Resturant logo");
        }else {
            mLoadingBar.setTitle("Setup You Profile");
            mLoadingBar.setMessage("Please wait,While Saving you data...");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mLogoRef.child(mUser.getUid()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        mLogoRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap=new HashMap();
                                hashMap.put("logoImageUrl",uri.toString());
                                hashMap.put("username",username);
                                hashMap.put("restuarentName",resturantName);
                                hashMap.put("streetAddress",streetAddress);
                                hashMap.put("city",city);
                                hashMap.put("country",country);
                                mAdminRef.child(mUser.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            SenduserToMainActivity();
                                            mLoadingBar.dismiss();
                                        }
                                        else {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(ctx,"Something going Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        mLoadingBar.dismiss();
                        Toast.makeText(ctx,"Something going Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}