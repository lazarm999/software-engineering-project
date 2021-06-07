package com.parovi.zadruga.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parovi.zadruga.R;
import com.parovi.zadruga.fragments.AdsFragment;
import com.parovi.zadruga.fragments.EmployerProfileFragment;
import com.parovi.zadruga.fragments.EmployersAdsFragment;
import com.parovi.zadruga.fragments.NewAdFragment;
import com.parovi.zadruga.fragments.NotificationsFragment;

public class MainEmployerActivity extends AppCompatActivity {

    BottomNavigationView bottom_nav;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        loadFragment(new AdsFragment());

        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav_employer);
        frame = findViewById(R.id.frame_employer_layout);

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()) {
                    case R.id.employerFeed:
                        fragment = new AdsFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.employerNotifications:
                        fragment = new NotificationsFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.employerNewAd:
                        fragment = new NewAdFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.employersAds:
                        fragment = new EmployersAdsFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.employerProfile:
                        fragment = new EmployerProfileFragment();
                        loadFragment(fragment);
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_employer_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}