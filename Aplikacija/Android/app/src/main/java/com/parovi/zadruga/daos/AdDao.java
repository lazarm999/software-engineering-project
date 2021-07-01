package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.nonEntityModels.AdWithTags;

import java.util.List;

@Dao
public abstract class AdDao extends BaseDao<Ad> {

    @Transaction
    @Query("SELECT Ad.*, user_table.*, Location.* " +
            "FROM Ad " +
            "INNER JOIN user_table ON user_table.userId = Ad.fkEmployerId " +
            "LEFT JOIN Location ON Location.locId = Ad.fkLocationId order by Ad.postTime desc limit :pageSkip, :pageSize")
    public abstract ListenableFuture<List<AdWithTags>> getAds(int pageSize, int pageSkip);

    @RawQuery(observedEntities = {Ad.class, User.class, Location.class, Tag.class})
    public abstract List<AdWithTags> getAds(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT Ad.*, user_table.*, Location.* " +
            "FROM Ad " +
            "INNER JOIN user_table ON user_table.userId = Ad.fkEmployerId " +
            "LEFT JOIN Location ON Location.locId = Ad.fkLocationId " +
            "WHERE Ad.adId = :adId")
    public abstract ListenableFuture<AdWithTags> getAdWithTagsById(int adId);

    @Query("SELECT * FROM Ad WHERE Ad.adId == :id")
    public abstract Ad getAdById(int id);

    @Query("SELECT * FROM Ad WHERE Ad.fkQbChatId LIKE :qbChatId")
    public abstract Ad getAdByQbChatId(String qbChatId);

    @Query("DELETE FROM Ad WHERE Ad.adId = :adId")
    public abstract ListenableFuture<Integer> deleteAdById(int adId);

    @Query("UPDATE Ad " +
            "SET fkQbChatId = :chatId "+
            "WHERE Ad.adId LIKE :adId")
    public abstract void updateQbChatId(String chatId, int adId);

    @Query("select a.* from Ad as a join AdTag " +
            "on a.adId  = AdTag.fkAdId where AdTag.fkTagId in (:tags) group by a.adId " +
            "order by count(*) desc, postTime desc limit :pageSkip, :pageSize")
    public abstract List<Ad> getAds(int pageSize, int pageSkip, List<Integer> tags);
}