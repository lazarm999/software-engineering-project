package com.parovi.zadruga.models.entityModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class University {
    @PrimaryKey
    private int universityId;
    private String name;

    public University(int universityId, String name) {
        this.universityId = universityId;
        this.name = name;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
