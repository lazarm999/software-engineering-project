package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.AdWithLocation;
import com.parovi.zadruga.models.crossRefModels.AdTagCrossRef;
import com.parovi.zadruga.models.manyToManyModels.AdWithTags;

import java.util.List;

@Dao
public interface AdDao {

    @Insert
    ListenableFuture<Long> insertAd(Ad ad);

    @Query("SELECT * FROM Ad")
    LiveData<List<Ad>> getAllAds();

    @Query("SELECT * FROM Ad INNER JOIN Location ON Ad.locationId = Location.locId")
    LiveData<List<AdWithLocation>> getAllAdsWithLocation();

    @Transaction
    @Query("SELECT * FROM Ad")
    LiveData<List<AdWithTags>> getAllAdsWithTags();

    @Insert
    ListenableFuture<Long> insertAdTagCrossRef(AdTagCrossRef adTagCrossRef);
}