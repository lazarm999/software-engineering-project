package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.Badge;
import com.parovi.zadruga.models.Tag;

@Dao
public interface TmpDao {
    @Insert
    ListenableFuture<Long> insertTag(Tag tag);

    @Insert
    ListenableFuture<Long> insertBadge(Badge badge);
}
