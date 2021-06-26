package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Tag;

import java.util.List;

@Dao
public abstract class TagDao extends BaseDao<Tag> {
    @Query("SELECT * FROM Tag")
    public abstract List<Tag> getAllTags();

    @Query("SELECT Tag.tagId FROM Tag WHERE Tag.tagId = :tagId")
    public abstract Integer getTagById(int tagId);

    @Query("SELECT Tag.* FROM AdTag " +
            "INNER JOIN Tag ON AdTag.fkTagId = Tag.tagId " +
            "WHERE AdTag.fkAdId = :adId")
    public abstract ListenableFuture<List<Tag>> getTagsByAdId(int adId);
}
