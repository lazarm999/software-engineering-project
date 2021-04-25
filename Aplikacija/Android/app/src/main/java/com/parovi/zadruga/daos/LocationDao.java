package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation(Location location);

    @Query("SELECT * FROM Location")
    LiveData<List<Location>> getAllLocations();
}
