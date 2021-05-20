package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.entityModels.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkNotifId", "fkReceiverId"},
        foreignKeys = {@ForeignKey(entity = Notification.class,
                        parentColumns = "notifId",
                        childColumns = "fkNotifId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkReceiverId",
                        onDelete = CASCADE)})
public class NotificationReceiver {
    @ColumnInfo(index = true)
    private int fkNotifId;
    @ColumnInfo(index = true)
    private int fkReceiverId;

    public NotificationReceiver(){ }

    public NotificationReceiver(int fkNotifId, int fkReceiverId) {
        this.fkNotifId = fkNotifId;
        this.fkReceiverId = fkReceiverId;
    }

    public int getFkNotifId() {
        return fkNotifId;
    }

    public void setFkNotifId(int fkNotifId) {
        this.fkNotifId = fkNotifId;
    }

    public int getFkReceiverId() {
        return fkReceiverId;
    }

    public void setFkReceiverId(int fkReceiverId) {
        this.fkReceiverId = fkReceiverId;
    }
}
