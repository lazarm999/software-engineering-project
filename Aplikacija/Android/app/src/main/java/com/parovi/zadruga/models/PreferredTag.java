package com.parovi.zadruga.models;

import androidx.room.Entity;

@Entity
public class PreferredTag {
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
