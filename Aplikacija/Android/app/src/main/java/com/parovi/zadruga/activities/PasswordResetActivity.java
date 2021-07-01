package com.parovi.zadruga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.databinding.ActivityPasswordResetBinding;
import com.parovi.zadruga.viewModels.ChangePasswordViewModel;

public class PasswordResetActivity extends AppCompatActivity {

    private ChangePasswordViewModel model;
    private ActivityPasswordResetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_password_reset);
        model = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model.getIsPassChanged().observe(this, customResponse -> {
            if(customResponse.getStatus() == CustomResponse.Status.OK){
                Intent intent;
                intent = new Intent(PasswordResetActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });


        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.txtPassword.getText().toString().equals(binding.txtConfirmPassword.getText().toString()))
                    model.changePass(binding.txtPassword.getText().toString());
                else
                    Toast.makeText(App.getAppContext(), "Pogresna lozinka", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}