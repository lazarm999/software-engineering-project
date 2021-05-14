package com.parovi.zadruga.models.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;
import com.parovi.zadruga.models.User;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(primaryKeys = {"fkUserId", "fkAdId"},
        foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkUserId",
                            onDelete = CASCADE),
                        @ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = CASCADE)})
public class Comment {
    @ColumnInfo(index = true)
    private int fkUserId;
    @ColumnInfo(index = true)
    private int fkAdId;
    private String commentText;
    private boolean isSynced;

    public Comment(int fkUserId, int fkAdId, String commentText) {
        this.fkUserId = fkUserId;
        this.fkAdId = fkAdId;
        this.commentText = commentText;
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

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
