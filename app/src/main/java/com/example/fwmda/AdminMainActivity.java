package com.example.fwmda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView profileAdmin, allUsersAdmin, donationsAdmin, logoutAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hello Welcome");


        profileAdmin = findViewById(R.id.profileAdmin);
        allUsersAdmin = findViewById(R.id.allUsersAdmin);
        donationsAdmin = findViewById(R.id.donationsAdmin);
        logoutAdmin = findViewById(R.id.logoutAdmin);

        logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}