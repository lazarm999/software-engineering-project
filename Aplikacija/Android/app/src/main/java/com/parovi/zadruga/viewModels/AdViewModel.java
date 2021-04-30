package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;
import com.parovi.zadruga.models.AdWithLocation;
import com.parovi.zadruga.repository.ZadrugaRepository;

import java.util.List;

public class AdViewModel extends AndroidViewModel {
    private LiveData<List<Ad>> ads;
    private LiveData<List<AdWithLocation>> adsWithLocation;
    private LiveData<List<Location>> locations;
    private ZadrugaRepository rep;

    public AdViewModel(@NonNull Application application) {
        super(application);
        rep = new ZadrugaRepository(application);
    }

    public LiveData<List<Ad>> getAds() {
        ads = rep.getAllAds();
        return ads;
    }

    public LiveData<List<AdWithLocation>> getAllAdsWithLocation() {
        adsWithLocation = rep.getAllAdsWithLocation();
        return adsWithLocation;
    }

    public LiveData<List<Location>> getAllLocations() {
        locations = rep.getAllLocations();
        return locations;
    }

    public void insertAd(Ad ad){
        rep.insertAd(ad);
    }

    public void insertLocation(Location location){
        rep.insertLocation(location);
    }
}
