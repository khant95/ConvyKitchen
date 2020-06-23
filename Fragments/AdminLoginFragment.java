package com.example.convykitchen.Fragments;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.convykitchen.AdminAuthActivity;
import com.example.convykitchen.AdminActivity;
import com.example.convykitchen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AdminLoginFragment extends Fragment {
    private TextInputLayout adminInputEmail,adminInputPassword;
    private Button adminBtnLogin;
    private TextView adminCreateNewAccount,adminForgotPassword;
    Context ctx;
    ProgressDialog mLoadingBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    public AdminLoginFragment(Context context) {
        ctx=context;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adminInputEmail=view.findViewById(R.id.admin_input_email);
        adminInputPassword=view.findViewById(R.id.admin_input_password);
        adminBtnLogin=view.findViewById(R.id.admin_btn_login);
        adminCreateNewAccount=view.findViewById(R.id.admin_create_new_account);
        adminForgotPassword=view.findViewById(R.id.admin_forgot_password);


        //progress
        mLoadingBar=new ProgressDialog(ctx);


        //firebase
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        adminForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminAuthActivity.fragmentManager.beginTransaction()
                        .replace(R.id.admin_container,new ForgotPasswordFragment(ctx)).commit();
            }
        });

        adminCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminAuthActivity.fragmentManager.beginTransaction()
                        .replace(R.id.admin_container,new AdminRegisterFragment(ctx)).commit();
            }
        });
        adminBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptLogin();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUser!=null)
        {
            AdminAuthActivity.fragmentManager.beginTransaction()
                    .replace(R.id.admin_container,new AdminSetupFragment(ctx)).commit();
        }
    }

    private void AtemptLogin() {

        final String email = adminInputEmail.getEditText().getText().toString();
        final String password = adminInputPassword.getEditText().getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(adminInputEmail, "Email is not Valid");
        } else if (password.isEmpty() || password.length() < 4) {
            showError(adminInputPassword, "Password lenght not greater then 4");
        } else {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait,While check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        SendToMainActivity();
                        mLoadingBar.dismiss();
                    }
                    else
                    {
                        mLoadingBar.dismiss();
                        Toast.makeText(ctx.getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void SendToMainActivity() {
        Intent intent=new Intent(ctx.getApplicationContext(), AdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}
