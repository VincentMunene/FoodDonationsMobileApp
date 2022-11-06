package com.example.fwmda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        adminLoginEmail = findViewById(R.id.adminLoginEmail);
        adminLoginPassword = findViewById(R.id.adminLoginPassword);
        adminForgotPassword = findViewById(R.id.adminForgotPassword);
        adminLoginButton = findViewById(R.id.adminLoginButton);

        loader = new ProgressDialog(this);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = adminLoginEmail.getText().toString().trim();
                final String password = adminLoginPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    adminLoginEmail.setError("Email is required");
                }
                if(TextUtils.isEmpty(password)){
                    adminLoginPassword.setError("Email is required");
                }
                else{
                    loader.setMessage("login in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!email.equals("fooddonationapplication@gmail.com")){
                                Toast.makeText(AdminLoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            if(!password.equals("1Northwest")){
                                Toast.makeText(AdminLoginActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(AdminLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            loader.dismiss();

                        }
                    });

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}