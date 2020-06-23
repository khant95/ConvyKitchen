package com.example.convykitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference mCutomerStorage;
    DatabaseReference mCustomerRef,FoodRef;

    Toolbar mToolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView profileHeaderImage;
    ProgressBar progressBar;
    TextView username;

    DatabaseReference RestuarentRef;
    FirebaseRecyclerAdapter<Restaurent,RestarentViewHolder>adapter;
    FirebaseRecyclerOptions<Restaurent>options;
    RecyclerView recyclerView;
    EditText Customer_input_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        //firebase
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mCustomerRef= FirebaseDatabase.getInstance().getReference().child("Customer");
        FoodRef= FirebaseDatabase.getInstance().getReference().child("Food").child(mUser.getUid());
        RestuarentRef= FirebaseDatabase.getInstance().getReference().child("Admin");
        CheckUserExistance();


        Customer_input_search=findViewById(R.id.Customer_input_search);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);


        mToolBar=findViewById(R.id.customer_app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Select Nearest Restaurant");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);



        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigation);
        progressBar=findViewById(R.id.progressBar);



        View v=navigationView.inflateHeaderView(R.layout.customer_drawer_header);
        profileHeaderImage=v.findViewById(R.id.profile_image);
        username=v.findViewById(R.id.restuarentname);

        LoadRestaurent("");
        Customer_input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    LoadRestaurent(s.toString());
                }
                else
                {
                    LoadRestaurent("");
                }

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        Intent intent=new Intent(CustomerActivity.this,CustomerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.profile:
                        startActivity(new Intent(CustomerActivity.this,ProfileActivity.class));
                         break;

                    case R.id.tableAvailbility:
                        Toast.makeText(CustomerActivity.this, "Select Restaurent Where You want to reserve Table", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;
                    case R.id.feedback:
                            startActivity(new Intent(CustomerActivity.this,SentOderActivity.class));
                         break;
                    case R.id.logout:
                        mAuth.signOut();
                        sendToUserSelection();
                        break;
                }
                return true;
            }
        });


    }

    private void LoadRestaurent(String s) {

        Query query=RestuarentRef.orderByChild("restuarentName").startAt(s).endAt(s+"\uf8ff");
            options=new FirebaseRecyclerOptions.Builder<Restaurent>().setQuery(query,Restaurent.class).build();
            adapter=new FirebaseRecyclerAdapter<Restaurent, RestarentViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull RestarentViewHolder holder, int position, @NonNull Restaurent model) {
                    Picasso.get().load(model.getLogoImageUrl()).placeholder(R.drawable.loader).into(holder.logo);
                  final String key=getRef(position).getKey();

                    holder.city.setText(model.getCity());
                    holder.address.setText(model.getStreetAddress());
                    holder.country.setText(model.getCountry());
                    holder.restuarentname.setText(model.getRestuarentName());

                    holder.btnTable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(CustomerActivity.this,TableAvailebilityActivity.class);
                            intent.putExtra("key",key);
                            startActivity(intent);
                        }
                    });
                    holder.btnOrderFood.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(CustomerActivity.this,FoodActivity.class);
                            intent.putExtra("key",key);
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public RestarentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_restuarent,parent,false);

                    return new RestarentViewHolder(v);
                }
            };
            adapter.startListening();
            recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendToUserSelection() {

        Intent intent=new Intent(this,UserSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void CheckUserExistance() {
        if (mUser==null)
        {
            sendUserToCustomerAuthActivity();
        }
        else
        {
            mCustomerRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists())
                    {
                        sendUserToCustomerSetupActivity();
                    }
                    else{
                       String  profileImage=dataSnapshot.child("profileImage").getValue().toString();
                        String fullName=dataSnapshot.child("fullName").getValue().toString();
                         Picasso.get().load(profileImage).placeholder(R.drawable.loader).into(profileHeaderImage);
                         username.setText(fullName);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendUserToCustomerSetupActivity() {
        Intent intent=new Intent(this,CustomerSetupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToCustomerAuthActivity() {
        Intent intent=new Intent(this,CustomerAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}