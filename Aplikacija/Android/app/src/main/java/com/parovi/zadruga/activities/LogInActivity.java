package com.parovi.zadruga.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parovi.zadruga.R;

public class LogInActivity extends AppCompatActivity {

    TextView change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        change = findViewById(R.id.txtChangePassword);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });
    }
}