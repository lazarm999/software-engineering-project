package com.parovi.zadruga.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityJobAdBinding;
import com.parovi.zadruga.viewModels.JobAdViewModel;

public class JobAdActivity extends AppCompatActivity {
    private JobAdViewModel model;
    private ActivityJobAdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(JobAdViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chats");
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this, R.id.job_ad_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }
}