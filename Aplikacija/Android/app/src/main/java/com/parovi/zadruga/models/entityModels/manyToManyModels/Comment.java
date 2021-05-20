package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.type.DateTime;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkUserId",
                            onDelete = CASCADE),
                        @ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = CASCADE)})
public class Comment {
    @PrimaryKey
    private int commentId;
    @ColumnInfo(index = true)
    private int fkUserId;
    @ColumnInfo(index = true)
    private int fkAdId;
    private String commentText;
    private long postedTime;
    private boolean isSynced;

    public Comment(int fkUserId, int fkAdId, String commentText, long postedTime) {
        this.fkUserId = fkUserId;
        this.fkAdId = fkAdId;
        this.commentText = commentText;
        this.postedTime = postedTime;
    }

    public Comment(int fkUserId, int fkAdId, String commentText) {
        this.fkUserId = fkUserId;
        this.fkAdId = fkAdId;
        this.commentText = commentText;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public long getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(long postedTime) {
        this.postedTime = postedTime;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
