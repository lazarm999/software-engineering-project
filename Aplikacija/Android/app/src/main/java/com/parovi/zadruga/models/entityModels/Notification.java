package com.parovi.zadruga.models.entityModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Notification
{
    @PrimaryKey
    private int notificationId;
    @ColumnInfo(index = true)
    private int fkAdId;
    @ColumnInfo(index = true)
    private int fkCommentId;
    @ColumnInfo(index = true)
    private int fkRatingId;
    private boolean accepted;
    private boolean tagged; //da li je tagovan ili je on objavio oglas pa je zbog toga dobio notif

    @Ignore
    private Ad ad;
    @Ignore
    private Comment comment;
    @Ignore
    private Rating rating;

    public Notification() {
    }

    public Notification(int notificationId, int fkAdId, int fkCommentId, int fkRatingId, boolean accepted, boolean tagged, Ad ad, Comment comment, Rating rating) {
        this.notificationId = notificationId;
        this.fkAdId = fkAdId;
        this.fkCommentId = fkCommentId;
        this.fkRatingId = fkRatingId;
        this.accepted = accepted;
        this.tagged = tagged;
        this.ad = ad;
        this.comment = comment;
        this.rating = rating;
    }

    public int getFkAdId() {
        return fkAdId;
    }

    public void setFkAdId(int fkAdId) {
        this.fkAdId = fkAdId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getFkCommentId() {
        return fkCommentId;
    }

    public void setFkCommentId(int fkCommentId) {
        this.fkCommentId = fkCommentId;
    }

    public int getFkRatingId() {
        return fkRatingId;
    }

    public void setFkRatingId(int fkRatingId) {
        this.fkRatingId = fkRatingId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isTagged() {
        return tagged;
    }

    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
