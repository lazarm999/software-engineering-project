package com.parovi.zadruga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.viewModels.AdViewModel;

public class MainActivity extends AppCompatActivity {

    private AdViewModel adViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tmp = (TextView) findViewById(R.id.tmpView);

        adViewModel = new ViewModelProvider(this).get(AdViewModel.class);
        adViewModel.getAds().observe(this, ads -> {
            if(ads != null && ads.size() > 0){
                Log.i("proba", ads.get(0).title);
            }
        });

        adViewModel.getAllAdsWithLocation().observe(this, ads -> {
            if(ads != null && ads.size() > 0){
                Log.i("proba", ads.get(0).ads.get(0).title);
            }
        });

        adViewModel.getAllLocations().observe(this, locs -> {
            if(locs != null && locs.size() > 0){
                Log.i("proba", locs.get(0).cityName);
            }
        });

        /*adViewModel.insertAd(new Ad("naslov1", "opis1", 1));
        adViewModel.insertAd(new Ad("naslov2", "opis2", 2));*/
    }
}