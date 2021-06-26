package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Tag;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(primaryKeys = {"fkAdId", "fkTagId"},
        foreignKeys = {@ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = SET_NULL,
                        onUpdate = CASCADE),
                        @ForeignKey(entity = Tag.class,
                        parentColumns = "tagId",
                        childColumns = "fkTagId",
                        onDelete = CASCADE,
                        onUpdate = CASCADE)})
public class AdTag {
    @ColumnInfo(index = true)
    private int fkAdId;
    @ColumnInfo(index = true)
    private int fkTagId;
    private boolean isSynced;

    public AdTag() {
    }

    public AdTag(int fkAdId, int fkTagId) {
        this.fkAdId = fkAdId;
        this.fkTagId = fkTagId;
    }

    public AdTag(int fkAdId, int fkTagId, boolean isSynced) {
        this.fkAdId = fkAdId;
        this.fkTagId = fkTagId;
        this.isSynced = isSynced;
    }

    public int getFkAdId() {
        return fkAdId;
    }

    public void setFkAdId(int fkAdId) {
        this.fkAdId = fkAdId;
    }

    public int getFkTagId() {
        return fkTagId;
    }

    public void setFkTagId(int fkTagId) {
        this.fkTagId = fkTagId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
