package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.android.material.circularreveal.CircularRevealHelper;
import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.Badge;
import com.parovi.zadruga.models.Tag;
import com.parovi.zadruga.models.TmpPost;
import com.parovi.zadruga.models.manyToManyModels.AdWithTags;

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
