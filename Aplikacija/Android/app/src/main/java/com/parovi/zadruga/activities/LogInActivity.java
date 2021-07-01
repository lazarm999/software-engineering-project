package com.parovi.zadruga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.viewModels.LoginViewModel;

public class LogInActivity extends AppCompatActivity {

   // private ActivityLogInBinding binding;
    private LoginViewModel model;

    TextView tvResetPass;
    Button btnLogIn;
    EditText username;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        model = new ViewModelProvider(this).get(LoginViewModel.class);

        username = findViewById(R.id.txtName);
        pass = findViewById(R.id.txtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);


        model.getUserType().observe(this, customResponse -> {
            if(customResponse.getStatus() == CustomResponse.Status.OK){
                Intent intent;
                String type = (String) customResponse.getBody();
                if(type.equals(Constants.ADMIN))
                    intent = new Intent(LogInActivity.this, AdminActivity.class);
                else if (type.equals(Constants.EMPLOYER))
                    intent = new Intent(LogInActivity.this, MainEmployerActivity.class);
                else
                    intent = new Intent(LogInActivity.this, MainStudentActivity.class);
                startActivity(intent);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//tea@gmail.com//
                if(username.getText().toString().equals("") || pass.getText().toString().equals(""))
                    Toast.makeText(App.getAppContext(), R.string.notAllFull, Toast.LENGTH_SHORT).show();
                else {
                    model.loginUser(username.getText().toString(), pass.getText().toString());
                }
            }
        });
    }
}