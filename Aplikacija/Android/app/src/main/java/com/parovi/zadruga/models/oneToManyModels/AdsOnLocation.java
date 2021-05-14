package com.parovi.zadruga.models.oneToManyModels;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;

import java.util.List;

public class AdsOnLocation {
    @Embedded
    public Location location;
    @Relation(entity = Ad.class,
    parentColumn = "locId",
    entityColumn = "locationId")
    public List<Ad> ads;
}