
package com.parovi.zadruga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.parovi.zadruga.models.crossRefModels.UserBadgeCrossRef;
import com.parovi.zadruga.models.manyToManyModels.AdWithTags;
import com.parovi.zadruga.models.manyToManyModels.UserWithBadges;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.viewModels.AdViewModel;

public class MainActivity extends AppCompatActivity {

    private AdViewModel adViewModel;
    private ZadrugaRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rep = new ZadrugaRepository(getApplication());
        setContentView(R.layout.activity_main);
        TextView tmp = (TextView) findViewById(R.id.tmpView);
        Button btn = (Button) findViewById(R.id.tmpBtn);
        adViewModel = new ViewModelProvider(this).get(AdViewModel.class);
        adViewModel.getAllAdsWithLocation().observe(this, ads -> {
            if(ads != null && ads.size() > 0){
                Log.i("proba", ads.get(0).ad.getTitle());
            }
        });
        rep.populateDb();

        rep.getAllAdsWithTags().observe(this, adWithTags -> {
            if(adWithTags != null && adWithTags.size() > 0){
                for(AdWithTags ad : adWithTags){
                    Log.i("user", ad.ad.getTitle());
                }
            }
        });
    }
}