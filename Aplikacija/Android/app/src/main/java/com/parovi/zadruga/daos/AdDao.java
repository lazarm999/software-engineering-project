package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.AdWithLocation;
import com.parovi.zadruga.models.manyToManyModels.AdTag;

import java.util.List;
/*TODO:
recimo mogu da se naprave razlicite vrste modela namenjene za razlicite activitije
u zavinosti koje informacije treba da se prikazu,
to kad tea i uros odluce kako ce da izgledaju aktiviji
isto vazi i za user-a tj ovo zapravo vazi za sve stvari koje treba da se prikazuju*/

@Dao
public interface AdDao {

    @Insert
    ListenableFuture<Long> insertAd(Ad ad);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAds(List<Ad> ads);

    @Update
    ListenableFuture<Integer>updateAd(Ad ad);

    @Query("SELECT * FROM Ad WHERE Ad.adId == :id")
    ListenableFuture<List<Ad>> getAdById(int id);

    @Query("SELECT * FROM Ad")
    ListenableFuture<List<Ad>> getAllAds();

    @Insert
    ListenableFuture<Long> insertAdTagCrossRef(AdTag adTag);

    /*void getAdsByLocation(List<Integer> locIds);
    void getAdsByFee(int from, int to);
    void getAdsByTag(int tagId);*/
}