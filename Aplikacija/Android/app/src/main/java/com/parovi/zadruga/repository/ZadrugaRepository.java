package com.parovi.zadruga.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.daos.TmpDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.database.ZadrugaDatabase;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Badge;
import com.parovi.zadruga.models.Location;
import com.parovi.zadruga.models.Tag;
import com.parovi.zadruga.models.User;
import com.parovi.zadruga.models.crossRefModels.AdTagCrossRef;
import com.parovi.zadruga.models.crossRefModels.UserBadgeCrossRef;
import com.parovi.zadruga.models.manyToManyModels.AdWithTags;
import com.parovi.zadruga.models.manyToManyModels.UserWithBadges;
import com.parovi.zadruga.models.oneToManyModels.AdsOnLocation;
import com.parovi.zadruga.models.AdWithLocation;

import java.util.List;

public class ZadrugaRepository {
    private AdDao adDao;
    private LocationDao locationDao;
    private TmpDao tmpDao;
    private UserDao userDao;
    private LiveData<List<Ad>> ads;
    private LiveData<List<AdsOnLocation>> adsWithLocation;

    public ZadrugaRepository(Application app){
        ZadrugaDatabase db = ZadrugaDatabase.getInstance(app);
        adDao = db.adDao();
        locationDao = db.locationDao();
        tmpDao = db.tmpDao();
        userDao = db.userDao();
    }
    //Ad
    public void insertAd(Ad ad){
        adDao.insertAd(ad);
    }
    public LiveData<List<Ad>> getAllAds(){
        return adDao.getAllAds();
    }
    public LiveData<List<AdWithLocation>> getAllAdsWithLocation(){
        return adDao.getAllAdsWithLocation();
    }

    public LiveData<List<AdWithTags>> getAllAdsWithTags(){
        return adDao.getAllAdsWithTags();
    }

    //Location
    public void insertLocation(Location location){
        locationDao.insertLocation(location);
    }
    public LiveData<List<Location>> getAllLocations(){
        return locationDao.getAllLocations();
    }

    //User
    public void insertUser(User user){
        userDao.insertUser(user);
    }

    public LiveData<List<UserWithBadges>> getAllUsersWithBadges(){
        return userDao.getAllUsersWithBadges();
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

    public void populateDb(){
        /*adDao.insertAd(new Ad("naslov1", "opis1", 1));
        adDao.insertAd(new Ad("naslov2", "opis2", 2));
        adDao.insertAd(new Ad("naslov3", "opis3", 1));
        adDao.insertAd(new Ad("naslov4", "opis4", 2));

        locationDao.insertLocation(new Location("nis"));
        locationDao.insertLocation(new Location("beograd"));

        tmpDao.insertTag(new Tag("basta"));
        tmpDao.insertTag(new Tag("kuca"));
        tmpDao.insertTag(new Tag("informatika"));
        tmpDao.insertTag(new Tag("baustela"));

        tmpDao.insertBadge(new Badge("pcelica"));
        tmpDao.insertBadge(new Badge("omiljeni radnik"));
        tmpDao.insertBadge(new Badge("secikesa"));
        tmpDao.insertBadge(new Badge("indijac"));

        adDao.insertAdTagCrossRef(new AdTagCrossRef(1, 2));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(1, 3));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(2, 4));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(3, 2));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(3, 1));*/
    }
}
