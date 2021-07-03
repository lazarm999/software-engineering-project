package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
/*(foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkRaterId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkRateeId",
                        onDelete = CASCADE)})*/
@Entity
public class Rating {
    @PrimaryKey
    private int ratingId;
    @ColumnInfo(index = true)
    private int fkRaterId;
    @ColumnInfo(index = true)
    @SerializedName("rateeId")
    private int fkRateeId;
    private Integer rating;
    private Date postTime;
    private String comment;
    private boolean isSynced;

    public Rating() {
    }

    public Rating(int ratingId, int fkRaterId, int fkRateeId, int rating, String comment, Date postTime) {
        this.ratingId = ratingId;
        this.fkRaterId = fkRaterId;
        this.fkRateeId = fkRateeId;
        this.rating = rating;
        this.postTime = postTime;
        this.comment = comment;
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

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
