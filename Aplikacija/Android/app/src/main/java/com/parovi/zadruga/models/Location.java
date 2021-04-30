package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    public int locId;
    public String cityName;

    public Location(String cityName) {
        this.cityName = cityName;
    }
}
