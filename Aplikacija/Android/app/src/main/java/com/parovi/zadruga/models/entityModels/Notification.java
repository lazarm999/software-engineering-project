package com.parovi.zadruga.models.entityModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import java.util.Date;

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
    private Boolean accepted;
    private Boolean tagged; //da li je tagovan ili je on objavio oglas pa je zbog toga dobio notif
    private Date postTime;
    @Ignore
    private String type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
}