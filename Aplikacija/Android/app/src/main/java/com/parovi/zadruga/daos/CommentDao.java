package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.nonEntityModels.CommentWithUser;

import java.util.List;

@Dao
public abstract class CommentDao extends BaseDao<Comment> {
    @Query("SELECT user_table.*, Comment.* FROM Comment " +
            "INNER JOIN user_table ON user_table.userId = Comment.fkUserId " +
            "WHERE Comment.fkAdId == :adId order by Comment.postTime asc")
    public abstract List<CommentWithUser> getCommentsByAdId(int adId);

    @Query("DELETE FROM Comment WHERE Comment.commentId = :commentId")
    public abstract ListenableFuture<Integer> deleteComment(int commentId);

    @Query("SELECT * FROM Comment WHERE Comment.commentId = :commentId")
    public abstract Comment getCommentById(int commentId);
}
