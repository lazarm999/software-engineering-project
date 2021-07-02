package com.parovi.zadruga.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
        //loadFragment(getFragmentToRun(fragCode));

        /*if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                *  && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);*/

        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav_student);

        bottom_nav.setOnNavigationItemSelectedListener(item -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.student_main_nav_host_frag);
            NavController navController = navHostFragment.getNavController();
            switch(item.getItemId()) {
                case R.id.studentFeed:
                    navController.navigate(R.id.action_global_adsFragment2);
                    //fragment = new AdsFragment();
                    break;
                case R.id.studentNotifications:
                    navController.navigate(R.id.action_global_notificationsFragment2);
                    //fragment = new NotificationsFragment();
                    break;
                case R.id.jobHistory:
                    navController.navigate(R.id.action_global_jobHistoryFragment2);
                    //fragment = new JobHistoryFragment();
                    break;
                case R.id.studentRecommendations:
                    navController.navigate(R.id.action_global_recommendationFragment);
                    //fragment = new RecommendationFragment();
                    break;
                default:
                    navController.navigate(R.id.action_global_studentProfileFragment5);
                    //fragment = new StudentProfileFragment();
                    break;
            }
            //loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.student_main_nav_host_frag, fragment);
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