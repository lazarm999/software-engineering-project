package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;

import java.util.Date;

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
    private String comment;
    private Date postTime;

    public Comment() {
    }

    public Comment(String comment) {
        this.comment = comment;
    }

    @Ignore
    public Comment(int commentId, int fkUserId, int fkAdId, String comment, Date postTime) {
        this.commentId = commentId;
        this.fkUserId = fkUserId;
        this.fkAdId = fkAdId;
        this.comment = comment;
        this.postTime = postTime;
    }

    @Ignore
    public Comment(int fkUserId, int fkAdId, String comment) {
        this.fkUserId = fkUserId;
        this.fkAdId = fkAdId;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
}
