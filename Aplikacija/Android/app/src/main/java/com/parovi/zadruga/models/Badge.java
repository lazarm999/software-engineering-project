package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Badge {
    @PrimaryKey
    public int badgeId;
    public String description; //description

    public Badge(String description) {
        this.description = description;
    }
}
