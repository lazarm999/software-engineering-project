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
    private Double latitude;
    private Double longitude;

    public Location() {
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return cityName;
    }

    public Location(int locId, String cityName) {
        this.locId = locId;
        this.cityName = cityName;
    }

    public Location(int locId, Double latitude, Double longitude) {
        this.locId = locId;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
