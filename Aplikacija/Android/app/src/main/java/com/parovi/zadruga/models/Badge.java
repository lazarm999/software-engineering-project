package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Badge {
    @PrimaryKey
    public int badgeId;
    public String desc; //description

    public Badge(String desc) {
        this.desc = desc;
    }
}
