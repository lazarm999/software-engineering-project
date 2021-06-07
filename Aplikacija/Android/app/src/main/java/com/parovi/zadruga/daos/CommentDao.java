package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.nonEntityModels.CommentWithUser;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT user_table.*, Comment.* FROM Comment " +
            "INNER JOIN user_table ON user_table.userId = Comment.fkUserId " +
            "WHERE Comment.fkAdId == :adId")
    ListenableFuture<List<CommentWithUser>> getCommentsByAdId(int adId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertComment(Comment comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertComments(List<Comment> comments);

    @Query("DELETE FROM Comment WHERE Comment.commentId = :commentId")
    ListenableFuture<Integer> deleteComment(int commentId);
}
