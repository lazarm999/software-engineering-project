package com.parovi.zadruga.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityMainEmployerBinding;
import com.parovi.zadruga.fragments.AdsFragment;
import com.parovi.zadruga.fragments.AdsFragmentDirections;
import com.parovi.zadruga.fragments.EmployerProfileFragment;
import com.parovi.zadruga.fragments.EmployersAdsFragment;
import com.parovi.zadruga.fragments.JobHistoryFragment;
import com.parovi.zadruga.fragments.NewAdFragment;
import com.parovi.zadruga.fragments.NewAdFragmentDirections;
import com.parovi.zadruga.fragments.NotificationsFragment;
import com.parovi.zadruga.fragments.NotificationsFragmentDirections;
import com.parovi.zadruga.viewModels.FeedViewModel;
import com.parovi.zadruga.viewModels.NewAdViewModel;

public class MainEmployerActivity extends AppCompatActivity {
    BottomNavigationView bottom_nav;
    FrameLayout frame;
    ActivityMainEmployerBinding binding;
    FeedViewModel feedViewModel;
    NewAdViewModel newAdViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        //newAdViewModel = new ViewModelProvider(this).get(NewAdViewModel.class);
        binding = ActivityMainEmployerBinding.inflate(getLayoutInflater());

        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav_employer);

        bottom_nav.setOnNavigationItemSelectedListener(item -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.employer_main_nav_host_frag);
            NavController navController = navHostFragment.getNavController();
            //Fragment fragment;
            //navController.popBackStack();
            switch(item.getItemId()) {
                case R.id.employerFeed:
                    navController.navigate(R.id.action_global_adsFragment);
                    //loadFragment(AdsFragment.class);
                    break;
                case R.id.employerNotifications:
                    navController.navigate(R.id.action_global_notificationsFragment);
                    //loadFragment(NotificationsFragment.class);
                    break;
                case R.id.employerNewAd:
                    navController.navigate(R.id.action_global_newAdFragment2);
                    //loadFragment(NewAdFragment.class);
                    break;
                case R.id.employersAds:
                    navController.navigate(R.id.action_global_jobHistoryFragment);
                    //loadFragment(JobHistoryFragment.class);
                    break;
                case R.id.employerProfile:
                    navController.navigate(R.id.action_global_employerProfileFragment3);
                    //loadFragment(EmployerProfileFragment.class);
                    break;
            }
            return true;
        });
    }

    private void loadFragment(Class<? extends Fragment> type) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.employer_main_nav_host_frag, type, null);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}