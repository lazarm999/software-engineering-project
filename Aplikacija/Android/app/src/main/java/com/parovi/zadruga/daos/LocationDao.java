package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;

import org.xml.sax.helpers.LocatorImpl;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    ListenableFuture<Long> insertLocation(Location location);

    @Query("SELECT * FROM Location")
    LiveData<List<Location>> getAllLocations();
}
