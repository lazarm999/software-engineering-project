package com.parovi.zadruga.models.entityModels;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Notification
{
    @PrimaryKey
    private int notificationId;
    @ColumnInfo(index = true)
    private Integer fkAdId;
    @ColumnInfo(index = true)
    private Integer fkCommentId;
    @ColumnInfo(index = true)
    private Integer fkRatingId;
    private Boolean accepted; //je l to da bude boolean ili Boolean
    private Boolean tagged; //da li je tagovan ili je on objavio oglas pa je zbog toga dobio notif

    @Ignore
    private Ad ad;
    @Ignore
    private CommentResponse comment;
    @Ignore
    private RatingResponse rating;

    public Notification() {
    }

    public Integer getFkAdId() {
        return fkAdId;
    }

    public void setFkAdId(Integer fkAdId) {
        this.fkAdId = fkAdId;
    }

    public Integer getFkCommentId() {
        return fkCommentId;
    }

    public void setFkCommentId(Integer fkCommentId) {
        this.fkCommentId = fkCommentId;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public RatingResponse getRating() {
        return rating;
    }

    public void setRating(RatingResponse rating) {
        this.rating = rating;
    }

    public CommentResponse getComment() {
        return comment;
    }

    public void setComment(CommentResponse comment) {
        this.comment = comment;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getFkRatingId() {
        return fkRatingId;
    }

    public void setFkRatingId(Integer fkRatingId) {
        this.fkRatingId = fkRatingId;
    }

    public Boolean getTagged() {
        return tagged;
    }

    public void setTagged(Boolean tagged) {
        this.tagged = tagged;
    }
}