package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;

import java.util.List;

@Dao
public interface RatingDao {
    /*TODO: ovde treba da ide neki dobudzeniji/prostiji user*/
    @Query("SELECT * FROM Rating " +
            "INNER JOIN user_table ON Rating.fkRaterId = user_table.userId " +
            "WHERE Rating.fkRatedId == :ratedId")
    LiveData<List<Comment>> getRatingByRatedId(int ratedId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertRating(Rating rating);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertRatings(List<Rating> ratings);
}
