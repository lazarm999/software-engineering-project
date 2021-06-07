package com.parovi.zadruga.models.nonEntityModels;

import androidx.room.Embedded;

import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;

public class CommentWithUser {
    @Embedded
    private User user;
    @Embedded
    private Comment comment;

    public CommentWithUser() {
    }

    public CommentWithUser(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
