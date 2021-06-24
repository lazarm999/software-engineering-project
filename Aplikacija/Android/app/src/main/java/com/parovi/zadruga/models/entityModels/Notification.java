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
    //TODO: da l ovde da pisem "fk" posto ce chat nece da se cuva u nasoj bazi
    private int fkChatId;
    private String type;
    private String title;
    private String desc;

    public Notification() {
    }

    @Ignore
    public Notification(String type) {
        this.type = type;
    }

    @Ignore
    public Notification(int notifId, int fkSenderId, int fkAdId, int fkChatId, String type, String title, String desc) {
        this.notifId = notifId;
        this.fkSenderId = fkSenderId;
        this.fkAdId = fkAdId;
        this.fkChatId = fkChatId;
        this.type = type;
        this.title = title;
        this.desc = desc;
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

    public int getFkChatId() {
        return fkChatId;
    }

    public void setFkChatId(int fkChatId) {
        this.fkChatId = fkChatId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitile() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
