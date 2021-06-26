package com.parovi.zadruga.models.entityModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Location {
    @PrimaryKey
    private int locId;
    private String cityName;

    public Location() {
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return cityName;
    }

    public Location(String cityName) {
        this.cityName = cityName;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
