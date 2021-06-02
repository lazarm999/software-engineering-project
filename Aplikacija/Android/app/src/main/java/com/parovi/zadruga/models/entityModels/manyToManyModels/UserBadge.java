package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkUserId", "fkBadgeId"},
        foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkUserId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = Badge.class,
                        parentColumns = "badgeId",
                        childColumns = "fkBadgeId",
                        onDelete = CASCADE)})
public class UserBadge {
    @ColumnInfo(index = true)
    private int fkUserId;
    @ColumnInfo(index = true)
    private int fkBadgeId;
    private boolean isSynced;

    public UserBadge() {
    }

    public UserBadge(int fkUserId, int fkBadgeId) {
        this.fkUserId = fkUserId;
        this.fkBadgeId = fkBadgeId;
    }

    public UserBadge(int fkUserId, int fkBadgeId, boolean isSynced) {
        this.fkUserId = fkUserId;
        this.fkBadgeId = fkBadgeId;
        this.isSynced = isSynced;
    }

    public int getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(int fkUserId) {
        this.fkUserId = fkUserId;
    }

    public int getFkBadgeId() {
        return fkBadgeId;
    }

    public void setFkBadgeId(int fkBadgeId) {
        this.fkBadgeId = fkBadgeId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
