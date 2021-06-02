package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.parovi.zadruga.models.entityModels.User;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkRaterId", "fkRateeId"},
        foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkRaterId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkRateeId",
                        onDelete = CASCADE)})
public class Rating {
    @ColumnInfo(index = true)
    private int fkRaterId;
    @ColumnInfo(index = true)
    private int fkRateeId;
    private int rating;
    private Date postTime;
    private String comment;
    private boolean isSynced;

    public Rating() {
    }

    @Ignore
    public Rating(int fkRaterId, int fkRateeId, int rating, String comment, Date postTime) {
        this.fkRaterId = fkRaterId;
        this.fkRateeId = fkRateeId;
        this.rating = rating;
        this.postTime = postTime;
        this.comment = comment;
    }

    @Ignore
    public Rating(int fkRaterId, int fkRateeId, int rating, String comment) {
        this.fkRaterId = fkRaterId;
        this.fkRateeId = fkRateeId;
        this.rating = rating;
        this.comment = comment;
    }

    @Ignore
    public Rating(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public int getFkRaterId() {
        return fkRaterId;
    }

    public void setFkRaterId(int fkRaterId) {
        this.fkRaterId = fkRaterId;
    }

    public int getFkRateeId() {
        return fkRateeId;
    }

    public void setFkRateeId(int fkRateeId) {
        this.fkRateeId = fkRateeId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
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

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
}
