package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityJobAdBinding;
import com.parovi.zadruga.viewModels.AdViewModel;

public class JobAdActivity extends AppCompatActivity {
    public static final String AD_ID = "AdId";

    private AdViewModel model;
    private ActivityJobAdBinding binding;
    private MutableLiveData<CustomResponse<?>> isLoggedIn = new MutableLiveData<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(AdViewModel.class);

        model.loadCredentials();
        model.load(extractAdId());
        //ActionBar actionBar = getSupportActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this, R.id.job_ad_nav_host_fragment);
    }
    private int extractAdId() {
        return getIntent().getIntExtra(AD_ID, 34);
    }
}