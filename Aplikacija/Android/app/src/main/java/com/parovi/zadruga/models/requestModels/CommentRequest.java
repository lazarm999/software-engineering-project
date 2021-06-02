package com.parovi.zadruga.models.requestModels;

public class CommentRequest {
    public String comment;

    public CommentRequest() {
    }

    public CommentRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
