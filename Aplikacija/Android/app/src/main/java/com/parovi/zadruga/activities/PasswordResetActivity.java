package com.parovi.zadruga.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parovi.zadruga.R;

public class PasswordResetActivity extends AppCompatActivity {

    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        reset = findViewById(R.id.btnResetPassword);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordResetActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}