package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Location;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    ListenableFuture<Long> insertLocation(Location location);

    @Query("SELECT * FROM Location")
    ListenableFuture<List<Location>> getAllLocations();

    @Query("SELECT * FROM Location WHERE Location.locId = :locId")
    ListenableFuture<Location> getLocationById(int locId);
}
