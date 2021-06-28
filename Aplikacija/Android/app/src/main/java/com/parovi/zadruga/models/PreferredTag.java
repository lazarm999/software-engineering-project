package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PreferredTag {
    @PrimaryKey
    private int tagId;

    public PreferredTag(int tagId) {
        this.tagId = tagId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
