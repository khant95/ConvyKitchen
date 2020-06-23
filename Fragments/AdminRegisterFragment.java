package com.example.convykitchen.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.convykitchen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminRegisterFragment extends Fragment {


    private TextInputLayout adminInputEmail,adminInputPassword,adminInputConformPassword;
    private Button adminBtnRegister;
    private TextView  adminAlreadyHaveAccount;
    ProgressDialog mLoadingBar;

    Context ctx;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String UserID;


    public AdminRegisterFragment(Context context) {
        ctx=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Varibales initlization
        adminInputEmail=view.findViewById(R.id.admin_input_email);
        adminInputPassword=view.findViewById(R.id.admin_input_password);
        adminInputConformPassword=view.findViewById(R.id.admin_input_conformPassword);
        adminAlreadyHaveAccount=view.findViewById(R.id.admin_already_have_account);
        adminBtnRegister=view.findViewById(R.id.admin_btn_register);

        //progressBar
        mLoadingBar=new ProgressDialog(ctx);

        //   firebase initlization
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();




        adminAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminAuthActivity.fragmentManager.beginTransaction()
                        .replace(R.id.admin_container,new AdminLoginFragment(ctx)).commit();
            }
        });
        adminBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtempRegistration();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUser!=null)
        {
            //AdminAuthActivity.fragmentManager.beginTransaction().replace()
            AdminAuthActivity.fragmentManager.beginTransaction()
                    .replace(R.id.admin_container,new AdminSetupFragment(ctx)).commit();
        }
    }

    private void AtempRegistration() {

        final String email = adminInputEmail.getEditText().getText().toString();
        final String password = adminInputPassword.getEditText().getText().toString();
        final String conformPassword = adminInputConformPassword.getEditText().getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(adminInputEmail, "Email is not Valid");
        } else if (password.isEmpty() || password.length() < 4) {
            showError(adminInputPassword, "Password lenght not greater then 4");
        } else if (conformPassword.isEmpty() || !conformPassword.equals(password)) {
            showError(adminInputConformPassword, "pasword not Match");

        } else {
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait,While check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ctx, "Registeration Succesfully!", Toast.LENGTH_SHORT).show();
                        AdminAuthActivity.fragmentManager.beginTransaction()
                                .replace(R.id.admin_container,new AdminSetupFragment(ctx)).commit();
                        mLoadingBar.dismiss();
                    } else {
                        Toast.makeText(ctx, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        mLoadingBar.dismiss();
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
