package com.example.convykitchen.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.convykitchen.AdminAuthActivity;
import com.example.convykitchen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {
    Context ctx;
    EditText inputEmail;
    ProgressBar progressBar;
    Button btnSendVerification;
    FirebaseAuth mAuth;


    public ForgotPasswordFragment(Context context) {
        // Required empty public constructor
        ctx = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputEmail=view.findViewById(R.id.inputEmailConfirmPassword);
        progressBar=view.findViewById(R.id.progressBar);
        btnSendVerification=view.findViewById(R.id.btnVerification);
        progressBar.setVisibility(View.GONE);

        //firebase
        mAuth=FirebaseAuth.getInstance();


        btnSendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptForgotPassword();
            }
        });
    }
    private void AtemptForgotPassword() {

        final String email = inputEmail.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputEmail, "Email is not Valid");
        }else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        progressBar.setVisibility(View.GONE);
                        AdminAuthActivity.fragmentManager.beginTransaction()
                                .replace(R.id.admin_container,new AdminLoginFragment(ctx)).commit();
                        Toast.makeText(ctx, "Check Your Email & Update password!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}