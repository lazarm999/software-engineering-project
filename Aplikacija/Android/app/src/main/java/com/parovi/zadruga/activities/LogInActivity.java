package com.parovi.zadruga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.App;
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

        tvResetPass = findViewById(R.id.txtChangePassword);
        username = findViewById(R.id.txtName);
        pass = findViewById(R.id.txtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);


        model.getIsEmployer().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    Intent intent;
                    if((Boolean) customResponse.getBody())
                        intent = new Intent(LogInActivity.this, MainEmployerActivity.class);
                    else
                        intent = new Intent(LogInActivity.this, MainStudentActivity.class);
                    startActivity(intent);
                }
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



        tvResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });
    }
}