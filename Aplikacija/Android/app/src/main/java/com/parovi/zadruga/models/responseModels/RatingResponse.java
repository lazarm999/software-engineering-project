package com.parovi.zadruga.models.responseModels;

import androidx.room.Embedded;
import androidx.room.Ignore;

import com.parovi.zadruga.models.entityModels.User;

import java.util.Date;

public class RatingResponse {
    @Embedded
    private User rater;
    private Date postTime;
    private int ratee;
    private int rating;
    private String comment;

    public User getRater() {
        return rater;
    }

    public void setRater(User rater) {
        this.rater = rater;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public int getRatee() {
        return ratee;
    }

    public void setRatee(int ratee) {
        this.ratee = ratee;
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
