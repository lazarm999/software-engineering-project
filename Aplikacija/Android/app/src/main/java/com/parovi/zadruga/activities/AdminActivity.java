package com.parovi.zadruga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityAdminBinding;
import com.parovi.zadruga.fragments.AdsFragment;
import com.parovi.zadruga.fragments.JobHistoryFragment;
import com.parovi.zadruga.fragments.NotificationsFragment;
import com.parovi.zadruga.fragments.RecommendationFragment;
import com.parovi.zadruga.fragments.ReportFragment;
import com.parovi.zadruga.fragments.StudentProfileFragment;

public class AdminActivity extends AppCompatActivity {
    public static final String FRAGMENT_SELECTION = "FragSel";
    public static final int FEED = 1, REPORTS = 2;
    BottomNavigationView bottom_nav;
    FrameLayout frame;
    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //int fragCode = getIntent().getIntExtra(FRAGMENT_SELECTION, FEED);
        //loadFragment(getFragmentToRun(fragCode));

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavAdmin.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_main_nav_host_frag);
                NavController navController = navHostFragment.getNavController();
                switch(item.getItemId()) {
                    case R.id.adminFeed:
                        navController.navigate(R.id.action_global_adsFragment3);
                        break;
                    case R.id.adminReports:
                        navController.navigate(R.id.action_global_reportFragment);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_main_nav_host_frag, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private Fragment getFragmentToRun(int fragCode) {
        Fragment fragment;
        switch (fragCode) {
            case FEED:
                fragment = new AdsFragment();
                break;
            case REPORTS:
                fragment = new ReportFragment();
                break;
            default:
                fragment = new AdsFragment();
                break;
        }
        return fragment;
    }
}