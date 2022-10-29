package com.example.fwmda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText adminLoginEmail, adminLoginPassword;
    private TextView adminForgotPassword;
    private Button adminLoginButton;

    private ProgressDialog loader;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_main);

        adminLoginEmail = findViewById(R.id.adminLoginEmail);
        adminLoginPassword = findViewById(R.id.adminLoginPassword);
        adminForgotPassword = findViewById(R.id.adminForgotPassword);
        adminLoginButton = findViewById(R.id.adminLoginButton);

        loader = new ProgressDialog(this);

    }
}