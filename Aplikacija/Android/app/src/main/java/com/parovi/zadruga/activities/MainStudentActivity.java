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
import com.parovi.zadruga.fragments.NotificationsFragment;
import com.parovi.zadruga.fragments.RecommendationFragment;
import com.parovi.zadruga.fragments.JobHistoryFragment;
import com.parovi.zadruga.fragments.StudentProfileFragment;


public class MainStudentActivity extends AppCompatActivity {
    public static final String FRAGMENT_SELECTION = "FragSel";
    public static final int FEED = 1, NOTIFICATIONS = 2, JOB_HISTORY = 3, RECOMMENDED = 4, PROFILE = 5;

    BottomNavigationView bottom_nav;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        int fragCode = getIntent().getIntExtra(FRAGMENT_SELECTION, FEED);
        loadFragment(getFragmentToRun(fragCode));

        /*if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                *  && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);*/

        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav_student);
        frame = findViewById(R.id.frame_student_layout);

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()) {
                    case R.id.studentFeed:
                        fragment = new AdsFragment();
                        break;
                    case R.id.studentNotifications:
                        fragment = new NotificationsFragment();
                        break;
                    case R.id.jobHistory:
                        fragment = new JobHistoryFragment();
                        break;
                    case R.id.studentRecommendations:
                        fragment = new RecommendationFragment();
                        break;
                    default:
                        fragment = new StudentProfileFragment();
                        break;
                }
                loadFragment(fragment);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_student_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private Fragment getFragmentToRun(int fragCode) {
        Fragment fragment;
        switch (fragCode) {
            case FEED:
                fragment = new AdsFragment();
                break;
            case NOTIFICATIONS:
                fragment = new NotificationsFragment();
                break;
            case JOB_HISTORY:
                fragment = new JobHistoryFragment();
                break;
            case RECOMMENDED:
                fragment = new RecommendationFragment();
                break;
            case PROFILE:
                fragment = new StudentProfileFragment();
                break;
            default:
                fragment = new AdsFragment();
                break;
        }
        return fragment;
    }
}