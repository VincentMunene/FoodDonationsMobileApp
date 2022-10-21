package com.example.fwmda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationsActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        textView = findViewById(R.id.text_view);

        NotificationManager manager = (NotificationManager)
                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
        if (getIntent().hasExtra("Yes")) {
            textView.setText("Donation added successfully pending approval");
            textView.setTextColor(Color.GREEN);
        } else if (getIntent().hasExtra("No")) {
            textView.setText("Donation not added try again...");
            textView.setTextColor(Color.RED);
        }


    }
}
