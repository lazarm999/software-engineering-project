package com.parovi.zadruga.models.crossRefModels;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Badge;
import com.parovi.zadruga.models.User;

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
public class UserBadgeCrossRef {
    private int fkUserId;
    private int fkBadgeId;

    public UserBadgeCrossRef(int fkUserId, int fkBadgeId) {
        this.fkUserId = fkUserId;
        this.fkBadgeId = fkBadgeId;
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

}
