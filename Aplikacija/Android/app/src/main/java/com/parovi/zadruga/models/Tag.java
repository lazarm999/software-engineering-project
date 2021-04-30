package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tag {
    @PrimaryKey(autoGenerate = true)
    public int tagId;
    public String name;

    public Tag(String name) {
        this.name = name;
    }
}
