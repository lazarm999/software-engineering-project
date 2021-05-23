package com.parovi.zadruga.models.nonEntityModels;

import com.google.type.DateTime;

public class CommentUser {
    private int commentId;
    private int fkAdId;
    private String commentText;
    private DateTime postedTime;
    private int userId;
    private String username;
    private String imageUrl;

    public CommentUser(String commentText, String username) {
        this.commentText = commentText;
        this.username = username;
    }

    public CommentUser(int commentId, int fkAdId, String commentText, DateTime postedTime,
                       int userId, String username, String imageUrl) {
        this.commentId = commentId;
        this.fkAdId = fkAdId;
        this.commentText = commentText;
        this.postedTime = postedTime;
        this.userId = userId;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public DateTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(DateTime postedTime) {
        this.postedTime = postedTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
