package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;

import static androidx.room.ForeignKey.CASCADE;
/*(primaryKeys = {"fkUserId", "fkAdId"},
        foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkUserId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = CASCADE)})*/
@Entity(primaryKeys = {"fkUserId", "fkAdId"})
public class Applied {
    private int fkUserId;
    @ColumnInfo(index = true)
    private int fkAdId;
    private boolean chosen;
    private boolean isSynced;

    public Applied() {
    }

    public Applied(int fkUserId, int fkAdId) {
        this.fkUserId = fkUserId;
        this.fkAdId = fkAdId;
    }

    public Applied(int fkUserId, int fkAdId, boolean chosen) {
        this.fkUserId = fkUserId;
        this.fkAdId = fkAdId;
        this.chosen = chosen;
    }

    public int getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(int fkUserId) {
        this.fkUserId = fkUserId;
    }

    public int getFkAdId() {
        return fkAdId;
    }

    public void setFkAdId(int fkAdId) {
        this.fkAdId = fkAdId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}

