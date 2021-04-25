package com.parovi.zadruga.models.crossRefModels;

import androidx.room.Entity;

@Entity(primaryKeys = {"adId", "tagId"})
public class AdTagCrossRef {
    public int adId;
    public int tagId;
}
