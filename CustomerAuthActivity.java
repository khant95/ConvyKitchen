package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomerAuthActivity extends AppCompatActivity {

    TextView customerAlert;
    CountryCodePicker country_code_picker;
    TextInputLayout customerPhoneNumber;
    Button customerBtnGenrate;
    ProgressBar progressBar;
    String Complete_phone;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_auth);



        //Varibale initilaization
        customerAlert=findViewById(R.id.customer_alert);
        country_code_picker=(CountryCodePicker) findViewById(R.id.ccp);
        customerPhoneNumber=findViewById(R.id.customer_phone);
        customerBtnGenrate=findViewById(R.id.customer_btn_genrate);
        progressBar=findViewById(R.id.progressBar);
        customerAlert.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        //Firebase
        mAuth=FirebaseAuth.getInstance();

        //button click listenr to genrate otp
        customerBtnGenrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtempLogin();
            }
        });

    }

    private void AtempLogin() {
        final String phone = customerPhoneNumber.getEditText().getText().toString().trim();
         Complete_phone="+"+country_code_picker.getFullNumber()+phone;

        if (phone.isEmpty()) {
            showError(customerPhoneNumber, "phone is not Valid");
        }
      else {
            PhoneAuth(Complete_phone);
       }

    }

    private void PhoneAuth(String phone) {

        progressBar.setVisibility(View.VISIBLE);
        customerBtnGenrate.setEnabled(false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                CustomerAuthActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            customerAlert.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            customerAlert.setText("Verification Failed!,Please try again");
            customerBtnGenrate.setEnabled(true);
            Toast.makeText(CustomerAuthActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("eee", "onVerificationFailed: "+e.toString());

        }


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Intent intent=new Intent(CustomerAuthActivity.this,CustomerOtpActivity.class);
            intent.putExtra("s",s);
            intent.putExtra("phone",Complete_phone);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    };

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(CustomerAuthActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            sendUserToCustomerActivity();

                            // ...
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
        Intent intent=new Intent(CustomerAuthActivity.this,CustomerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
