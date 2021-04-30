package com.parovi.zadruga.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;

public class AdWithLocation {
    @Embedded
    public Ad ad;
    @Embedded()
    public Location location;
}
