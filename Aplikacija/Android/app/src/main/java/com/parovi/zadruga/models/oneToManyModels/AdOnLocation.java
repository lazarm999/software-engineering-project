package com.parovi.zadruga.models.oneToManyModels;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;

import java.util.List;

public class AdOnLocation {
    @Embedded
    public Location location;
    @Relation(entity = Ad.class,
    parentColumn = "id",
    entityColumn = "locationId")
    public List<Ad> ads;
}
