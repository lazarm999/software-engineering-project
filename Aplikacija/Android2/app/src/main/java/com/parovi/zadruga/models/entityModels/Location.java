package com.parovi.zadruga.models.entityModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*TODO: da li ce korisnik moci da doda proizvoljnu lokaciju ili ne?*/

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    public int locId;
    public String cityName;

    public Location(String cityName) {
        this.cityName = cityName;
    }
}
