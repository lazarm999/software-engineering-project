package com.parovi.zadruga.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.database.ZadrugaDatabase;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;
import com.parovi.zadruga.models.oneToManyModels.AdOnLocation;

import java.util.List;

public class ZadrugaRepository {
    private AdDao adDao;
    private LocationDao locationDao;
    private LiveData<List<Ad>> ads;
    private LiveData<List<AdOnLocation>> adsWithLocation;

    public ZadrugaRepository(Application app){
        ZadrugaDatabase db = ZadrugaDatabase.getInstance(app);
        adDao = db.adDao();
        locationDao = db.locationDao();
    }

    public LiveData<List<Ad>> getAllAds(){
        return adDao.getAllAds();
    }

    public LiveData<List<AdOnLocation>> getAllAdsWithLocation(){
        return adDao.getAllAdsWithLocation();
    }

    public LiveData<List<Location>> getAllLocations(){
        return locationDao.getAllLocations();
    }

    public void insertAd(Ad ad){
        new InsertAdAsyncTask(adDao).execute(ad);
    }

    public void insertLocation(Location l){
        new InsertLocationAsyncTask(locationDao).execute(l);
    }

    private static class InsertAdAsyncTask extends AsyncTask<Ad, Void, Void>{
        private AdDao adDao;

        private InsertAdAsyncTask(AdDao adDao){
            this.adDao = adDao;
        }

        @Override
        protected Void doInBackground(Ad... ads) {
            adDao.insertAd(ads[0]);
            return null;
        }
    }

    private static class InsertLocationAsyncTask extends AsyncTask<Location, Void, Void>{
        private LocationDao locationDao;

        private InsertLocationAsyncTask(LocationDao adDao){
            this.locationDao = adDao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDao.insertLocation(locations[0]);
            return null;
        }
    }
}
