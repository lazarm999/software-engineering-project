package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.TmpPost;

import java.util.List;

@Dao
public interface TmpDao {
    @Insert
    ListenableFuture<Long> insertTag(Tag tag);

    @Insert
    ListenableFuture<Long> insertBadge(Badge badge);

    @Query("SELECT * FROM tmppost")
    ListenableFuture<List<TmpPost>> getAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertPosts(List<TmpPost> posts);

    @Query("DELETE FROM TmpPost")
    ListenableFuture<Integer> deleteAllPosts();
}
