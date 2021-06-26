package com.parovi.zadruga.daos.lookupDaos;

import androidx.room.Dao;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.daos.BaseDao;
import com.parovi.zadruga.models.entityModels.Location;

import java.util.List;

@Dao
public abstract class LocationDao extends BaseDao<Location> {
    @Query("SELECT * FROM Location")
    public abstract ListenableFuture<List<Location>> getAllLocations();

    @Query("SELECT * FROM Location WHERE Location.locId = :locId")
    public abstract ListenableFuture<Location> getLocationById(int locId);
}
