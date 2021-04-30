package com.parovi.zadruga.models.crossRefModels;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Tag;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkAdId", "fkTagId"},
        foreignKeys = {@ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = Tag.class,
                        parentColumns = "tagId",
                        childColumns = "fkTagId",
                        onDelete = CASCADE)})
public class AdTagCrossRef {
    public int fkAdId;
    public int fkTagId;

    public AdTagCrossRef(int fkAdId, int fkTagId) {
        this.fkAdId = fkAdId;
        this.fkTagId = fkTagId;
    }
}
