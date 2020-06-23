package com.example.convykitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.convykitchen.Fragments.AdminRegisterFragment;

public class AdminAuthActivity extends AppCompatActivity {
 public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_auth);

        //Initilization
        fragmentManager=getSupportFragmentManager();


        //Add Fragments to Container.
      if (findViewById(R.id.admin_container)!=null)
      {
          if (savedInstanceState!=null)
          {
              return;
          }
          fragmentManager.beginTransaction().add(R.id.admin_container,new AdminRegisterFragment(this)).commit();
      }
    }
}
