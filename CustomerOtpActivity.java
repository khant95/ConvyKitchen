package com.example.convykitchen;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.chaos.view.PinView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class CustomerOtpActivity extends AppCompatActivity {
    PinView pinView;
    Button customerBtnVerifiy;
    FirebaseAuth mAuth;
    String s;
    String number;
    TextView textAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_otp);

        pinView=findViewById(R.id.pinView);
        customerBtnVerifiy=findViewById(R.id.customer_btn_very);
        textAlert=findViewById(R.id.customer_sent_otp_textView);

        //firebase
        mAuth=FirebaseAuth.getInstance();


        //get data from intent
        s=getIntent().getStringExtra("s");
        number=getIntent().getStringExtra("phone");


        textAlert.setText("We have sent OPT code to your given \n number " + number+" Edit?");

        customerBtnVerifiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, pinView.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
        textAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senToCutomerAuthActivity();
            }
        });

    }

    private void senToCutomerAuthActivity() {
        Intent intent=new Intent(this,CustomerAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(CustomerOtpActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            sendUserToCustomerActivity();


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("ResultZZ", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void sendUserToCustomerActivity() {
        Intent intent=new Intent(this,CustomerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}