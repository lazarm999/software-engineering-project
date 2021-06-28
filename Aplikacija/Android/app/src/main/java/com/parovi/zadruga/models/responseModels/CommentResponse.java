package com.parovi.zadruga.models.responseModels;

import android.graphics.Bitmap;

import com.parovi.zadruga.models.entityModels.User;

import java.util.Date;
import java.util.List;

public class CommentResponse {
    private int id;
    private int ad;
    private String comment;
    private Date postTime;
    private User user;
    private Bitmap userImage;
    private List<Integer> taggedIndices;

    public CommentResponse() {
    }

    public CommentResponse(int id, int ad, String comment, Date postTime){
        this.ad = ad;
        this.comment = comment;
        this.postTime = postTime;
    }

    public CommentResponse(int id, int ad, String comment, Date postTime, User user) {
        this.id = id;
        this.ad = ad;
        this.comment = comment;
        this.postTime = postTime;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAd() {
        return ad;
    }

    public void setAd(int ad) {
        this.ad = ad;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public List<Integer> getTaggedIndices() {
        return taggedIndices;
    }

    public void setTaggedIndices(List<Integer> taggedIndices) {
        this.taggedIndices = taggedIndices;
    }
}
