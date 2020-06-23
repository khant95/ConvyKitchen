package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

public class CustomerSetupActivity extends AppCompatActivity {

    TextInputLayout customerInputFullName,
            customerInputStreetAddress,customerInputCity,customerInputCountry;
    ImageView customerProfileImage;
    TextView customerImageText;
    Button customerBtnUpdate;
    public static final int IMAGE_REQUEST_CODE_POST=102;
    Uri uri=null;
    ProgressDialog mLoadingBar;
    DatabaseReference mCustomerRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference mCutomerStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_setup);


        //Variable initilization
        customerInputFullName=findViewById(R.id.Customer_input_fullName);
        customerInputStreetAddress=findViewById(R.id.Customer_input_Address);
        customerInputCity=findViewById(R.id.Customer_input_City);
        customerInputCountry=findViewById(R.id.Customer_input_Country);
        customerProfileImage=findViewById(R.id.Customer_logo_resturant);
        customerImageText=findViewById(R.id.Customer_logo_resturant_text);
        customerBtnUpdate=findViewById(R.id.Customer_btn_setup);

        mLoadingBar=new ProgressDialog(this);

        //Firebase
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mCustomerRef= FirebaseDatabase.getInstance().getReference().child("Customer");
        mCutomerStorage= FirebaseStorage.getInstance().getReference().child("CutomerProileImage");


        customerBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptSetup();
            }
        });
        customerProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST_CODE_POST);
            }
        });
        customerImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST_CODE_POST);
            }
        });


    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST_CODE_POST && resultCode==RESULT_OK && data!=null)
        {
            uri=data.getData();
            customerProfileImage.setImageURI(uri);
        }
    }

    private void AtemptSetup() {

        final String fullName = customerInputFullName.getEditText().getText().toString();
        final String address = customerInputStreetAddress.getEditText().getText().toString();
        final String city = customerInputCity.getEditText().getText().toString();
        final String country = customerInputCountry.getEditText().getText().toString();

        if (fullName.isEmpty() || fullName.length()<4) {
            showError(customerInputFullName, "Username is not Valid");
        }else if (address.isEmpty() || address.length()<4) {
            showError(customerInputStreetAddress, "Street Address must Greater than 10 Latter");
        }else if (city.isEmpty() || city.length()<4) {
            showError(customerInputCity, "City must greater than 3 latter");
        }
        else if (country.isEmpty() || country.length()<3) {
            showError(customerInputCountry, "Country must greater than 3 latter");
        } else if (uri==null ) {
            customerImageText.setText("Please Select your Profile logo");
        }else {
            mLoadingBar.setTitle("Setup You Profile");
            mLoadingBar.setMessage("Please wait,While Saving your data...");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mCutomerStorage.child(mUser.getUid()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        mCutomerStorage.child(mUser.getUid()).getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap=new HashMap();
                                hashMap.put("profileImage",uri.toString());
                                hashMap.put("fullName",fullName);
                                hashMap.put("phone",mUser.getPhoneNumber());
                                hashMap.put("streetAddress",address);
                                hashMap.put("city",city);
                                hashMap.put("country",country);
                                mCustomerRef.child(mUser.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            SendToCustomeHomeActivity();
                                            mLoadingBar.dismiss();
                                        }
                                        else {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(CustomerSetupActivity.this,"Something going Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerSetupActivity.this, "Try Again"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mLoadingBar.dismiss();
                        Toast.makeText(CustomerSetupActivity.this,"Something going Wrong Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void SendToCustomeHomeActivity() {
        Intent intent=new Intent(this,CustomerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}