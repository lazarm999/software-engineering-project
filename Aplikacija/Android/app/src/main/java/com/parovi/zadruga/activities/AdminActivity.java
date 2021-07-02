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
import com.parovi.zadruga.fragments.ReportFragment;

public class AdminActivity extends AppCompatActivity {
    public static final String FRAGMENT_SELECTION = "FragSel";
    public static final int FEED = 1, REPORTS = 2;
    BottomNavigationView bottom_nav;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        int fragCode = getIntent().getIntExtra(FRAGMENT_SELECTION, FEED);
        loadFragment(getFragmentToRun(fragCode));

        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav_admin);
        frame = findViewById(R.id.frame_admin_layout);

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()) {
                    case R.id.adminFeed:
                        fragment = new AdsFragment();
                        break;
                    default:
                        fragment = new ReportFragment();
                        break;
                }
                loadFragment(fragment);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_admin_layout, fragment);
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