package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.App;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Applied;

import java.util.List;

@Dao
public abstract class AppliedDao extends BaseDao<Applied>{
    @Query("SELECT * FROM Applied " +
            "INNER JOIN user_table ON Applied.fkAdId = user_table.userId " +
            "WHERE Applied.fkAdId = :adId")
    public abstract ListenableFuture<List<User>> getAppliedByAdId(int adId);

    @Query("SELECT COUNT(*) FROM Applied WHERE Applied.fkAdId = :adId")
    public abstract ListenableFuture<Integer> getNumOfApplied(int adId);

    @Delete
    public abstract ListenableFuture<Integer> deleteApplied(Applied applied);
}
