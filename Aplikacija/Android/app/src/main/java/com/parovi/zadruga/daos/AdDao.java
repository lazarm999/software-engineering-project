package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.oneToManyModels.AdOnLocation;

import java.util.List;

@Dao
public interface AdDao {

    @Insert
    void insertAd(Ad ad);

    @Query("SELECT * FROM Ad")
    LiveData<List<Ad>> getAllAds();

    @Transaction
    @Query("SELECT * FROM Ad INNER JOIN Location ON Ad.locationId = Location.id;")
    //@Query("SELECT * FROM Location")
    LiveData<List<AdOnLocation>> getAllAdsWithLocation();
}