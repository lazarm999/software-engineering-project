package com.parovi.zadruga.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityJobAdBinding;
import com.parovi.zadruga.viewModels.AdViewModel;

public class JobAdActivity extends AppCompatActivity {
    private AdViewModel model;
    private ActivityJobAdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(AdViewModel.class);

        //ActionBar actionBar = getSupportActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this, R.id.job_ad_nav_host_fragment);
    }
}