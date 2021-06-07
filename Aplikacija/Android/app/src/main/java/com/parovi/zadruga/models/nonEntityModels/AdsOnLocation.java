package com.parovi.zadruga.models.nonEntityModels;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Location;

import java.util.List;

public class AdsOnLocation {
    @Embedded
    public Location location;
    @Relation(entity = Ad.class,
                parentColumn = "locId",
                entityColumn = "locationId")
    public List<Ad> ads;
}