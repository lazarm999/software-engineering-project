package com.parovi.zadruga.models.entityModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkSenderId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = CASCADE)})
public class Notification
{
    @PrimaryKey(autoGenerate = true)
    private int notifId;
    @ColumnInfo(index = true)
    private int fkSenderId;
    @ColumnInfo(index = true)
    private int fkAdId;
    private String type;
    private String comment;
    private int rating;

    public Notification() {
    }

    @Ignore
    public Notification(String type) {
        this.type = type;
    }

    @Ignore
    public Notification(int notifId, int fkSenderId, int fkAdId, int fkChatId, String type) {
        this.notifId = notifId;
        this.fkSenderId = fkSenderId;
        this.fkAdId = fkAdId;
        this.type = type;
    }

    public int getNotifId() {
        return notifId;
    }

    public void setNotifId(int notifId) {
        this.notifId = notifId;
    }

    public int getFkSenderId() {
        return fkSenderId;
    }

    public void setFkSenderId(int fkSenderId) {
        this.fkSenderId = fkSenderId;
    }

    public int getFkAdId() {
        return fkAdId;
    }

    public void setFkAdId(int fkAdId) {
        this.fkAdId = fkAdId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
