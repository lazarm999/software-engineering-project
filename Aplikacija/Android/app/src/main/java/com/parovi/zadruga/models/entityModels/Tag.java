package com.parovi.zadruga.models.entityModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tag {
    @PrimaryKey
    private int tagId;
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
