package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import java.util.List;

@Dao
public abstract class RatingDao extends BaseDao<Rating> {
    /*TODO: ovde treba da ide neki dobudzeniji/prostiji user*/
    @Query("SELECT user_table.*, Rating.*, Rating.fkRateeId as ratee FROM Rating " +
            "INNER JOIN user_table ON Rating.fkRaterId = user_table.userId " +
            "WHERE Rating.fkRateeId == :rateeId")
    public abstract ListenableFuture<List<RatingResponse>> getRatingByRatedId(int rateeId);

    @Query("DELETE FROM Rating WHERE Rating.fkRaterId = :raterId AND Rating.fkRateeId = :rateeId")
    public abstract ListenableFuture<Integer> deleteRating(int raterId, int rateeId);
}
