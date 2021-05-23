package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.User;

import java.util.List;

@Dao
public interface AppliedDao {

    /*TODO: ovde treba da ide neki dobudzeniji/prostiji user*/
    @Query("SELECT * FROM Applied " +
            "INNER JOIN user_table ON Applied.fkAdId = user_table.userId " +
            "WHERE Applied.fkAdId = :adId")
    ListenableFuture<List<User>> getAppliedByAdId(int adId);

    @Query("SELECT COUNT(*) FROM Applied WHERE Applied.fkAdId = :adId")
    ListenableFuture<Integer> getNumOfApplied(int adId);
}
