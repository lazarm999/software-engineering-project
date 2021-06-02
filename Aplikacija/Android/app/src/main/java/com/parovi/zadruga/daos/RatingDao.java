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
public interface RatingDao {
    /*TODO: ovde treba da ide neki dobudzeniji/prostiji user*/
    @Query("SELECT user_table.*, Rating.*, Rating.fkRateeId as ratee FROM Rating " +
            "INNER JOIN user_table ON Rating.fkRaterId = user_table.userId " +
            "WHERE Rating.fkRateeId == :rateeId")
    ListenableFuture<List<RatingResponse>> getRatingByRatedId(int rateeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertRating(Rating rating);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertRatings(List<Rating> ratings);

    @Query("DELETE FROM Rating WHERE Rating.fkRaterId = :raterId AND Rating.fkRateeId = :rateeId")
    ListenableFuture<Integer> deleteRating(int raterId, int rateeId);
}
