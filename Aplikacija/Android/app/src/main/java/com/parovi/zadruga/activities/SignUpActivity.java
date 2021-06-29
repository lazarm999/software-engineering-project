package com.parovi.zadruga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivitySignUpBinding;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.viewModels.SIgnUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    private SIgnUpViewModel model;
    private ActivitySignUpBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(SIgnUpViewModel.class);



        binding.checkBoxE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.checkBoxS.setEnabled(false);
                if(!binding.checkBoxE.isChecked())
                    binding.checkBoxS.setEnabled(true);
            }
        });

        binding.checkBoxS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.checkBoxE.setEnabled(false);
                if(!binding.checkBoxS.isChecked())
                    binding.checkBoxE.setEnabled(true);
            }
        });

        binding.txtLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        binding.checkBoxM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.checkBoxF.setEnabled(false);
                if(!binding.checkBoxM.isChecked())
                    binding.checkBoxF.setEnabled(true);
            }
        });

        binding.checkBoxF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.checkBoxM.setEnabled(false);
                if(!binding.checkBoxF.isChecked())
                    binding.checkBoxM.setEnabled(true);

            }
        });

        model.getIsSignedUp().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    Intent intent;
                    intent = new Intent(SignUpActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//tea@gmail.com//
                if(binding.txtUsername.getText().toString().equals("") || binding.txtName.getText().toString().equals("") || binding.txtSurname.getText().toString().equals("") || binding.txtEmail.getText().toString().equals("") || binding.txtPass.getText().toString().equals("") || (!binding.checkBoxE.isChecked() && !binding.checkBoxS.isChecked()))
                    Toast.makeText(App.getAppContext(), R.string.notAllFull, Toast.LENGTH_SHORT).show();
                else {
                    user = new User(binding.txtUsername.getText().toString(), binding.txtName.getText().toString(),
                            binding.txtSurname.getText().toString(), binding.txtEmail.getText().toString(), binding.txtPass.getText().toString(), binding.checkBoxE.isChecked(), binding.txtMobile.getText().toString() == null ? null : binding.txtMobile.getText().toString());
                    model.signUp(user, binding.txtPass.getText().toString());
                    Toast.makeText(App.getAppContext(), "Success sign up", Toast.LENGTH_SHORT).show();
//                    Intent intent;
//
//                    intent = new Intent(SignUpActivity.this, LogInActivity.class);
//
//                    startActivity(intent);
                }
            }
        });
    }
}