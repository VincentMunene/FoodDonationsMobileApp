package com.example.fwmda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fwmda.Adapter.UserAdapter;
import com.example.fwmda.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_profile_image;
    private TextView nav_fullname, nav_email, nav_type;
    //private TextView nav_donorgroup;

    private DatabaseReference userRef;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<User> userList;
    private UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Food Donation Application");

        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.progressbar);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter =  new UserAdapter(MainActivity.this, userList);

        recyclerView.setAdapter(userAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();
                if (type.equals("donor")){
                    readOrganizations();
                }if (type.equals("organization")){
                    readDonors();
                }
                else {
                    readDonors();
                    readOrganizations();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname = nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        //nav_donorgroup = nav_view.getHeaderView(0).findViewById(R.id.nav_user_donorgroup);
        nav_type = nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    nav_fullname.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    //String donorgroup = snapshot.child("donorgroup").getValue().toString();
                    //nav_donorgroup.setText(donorgroup);

                    String type = snapshot.child("type").getValue().toString();
                    nav_type.setText(type);

                    if (snapshot.hasChild("profilepictureurl")) {
                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
                        //retrieving data from database to navigation drawer
                        //retrieving image use the glide dependency
                    }
                    else {
                        nav_profile_image.setImageResource(R.drawable.profile_image);
                    }

                    Menu nav_menu = nav_view.getMenu();

                    if (type.equals("donor")){
                        nav_menu.findItem(R.id.sentEmail).setTitle("Messages");
                        nav_menu.findItem(R.id.addDonation).setVisible(true);
                        nav_menu.findItem(R.id.myDonations).setVisible(true);
                    }
                    if (type.equals("organization")){
                        nav_menu.findItem(R.id.newDonations).setVisible(true);
                        nav_menu.findItem(R.id.receivedDonations).setVisible(true);
                    }
                    else {
                        nav_menu.findItem(R.id.receivedDonations).setVisible(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Organizations", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } //Fetch organization data to mainActivity to be viewed by Donors
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readOrganizations() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("organization");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Donors", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } //Fetch donor data to mainActivity to be viewed by organizations
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.individual:
                Intent intent3 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent3.putExtra("group", "individual");
                startActivity(intent3);
                break;

            case R.id.restaurant:
                Intent intent4 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent4.putExtra("group", "restaurant");
                startActivity(intent4);
                break;

            case R.id.profile:
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                break;

            case R.id.notifications:
                Intent intent7 = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent7);
                break;

            case R.id.sentEmail:
                Intent intent6 = new Intent(MainActivity.this, SendEmailActivity.class);
                startActivity(intent6);
                break;


            case R.id.compatible:
                Intent intent5 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent5.putExtra("group", "Compatible with me");
                startActivity(intent5);
                break;

            case R.id.addDonation:
                Intent intent8 = new Intent(MainActivity.this, AddDonationActivity.class);
                intent8.putExtra("group", "Add Donation");
                startActivity(intent8);
                break;

            case R.id.newDonations:
                Intent intent9 = new Intent(MainActivity.this,NewDonationsActivity.class);
                startActivity(intent9);
                break;




        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}