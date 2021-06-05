package com.parovi.zadruga.models.entityModels;

import androidx.room.Embedded;

public class AdWithLocation {
    @Embedded
    public Ad ad;
    @Embedded()
    public Location location;
}
