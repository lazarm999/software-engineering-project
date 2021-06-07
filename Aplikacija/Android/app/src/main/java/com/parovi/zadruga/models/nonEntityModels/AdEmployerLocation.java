package com.parovi.zadruga.models.nonEntityModels;

import androidx.room.Embedded;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.User;

import de.measite.minidns.record.A;

public class AdEmployerLocation {
    @Embedded
    private Ad ad;
    @Embedded(prefix = "")
    private User employer;
    @Embedded
    private Location location;

    public AdEmployerLocation() {
        ad = new Ad();
        employer = new User();
        location = new Location();
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
