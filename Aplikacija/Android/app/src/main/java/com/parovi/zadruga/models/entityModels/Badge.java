package com.parovi.zadruga.models.entityModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Badge {
    @PrimaryKey
    public int badgeId;
    public String description;

    public Badge(String description) {
        this.description = description;
    }

    public int getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(int badgeId) {
        this.badgeId = badgeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
