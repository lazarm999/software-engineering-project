package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment WHERE Comment.fkAdId == :adId")
    LiveData<List<Comment>> getCommentsByAdId(int adId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertComment(Comment comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertComments(List<Comment> comments);
}
