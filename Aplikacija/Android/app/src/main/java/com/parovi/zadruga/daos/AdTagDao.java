package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;

@Dao
public abstract class AdTagDao extends BaseDao<AdTag> {
    @Query("DELETE FROM AdTag WHERE AdTag.fkTagId = :tagId AND AdTag.fkAdId = :adId")
    public abstract Integer deleteAdTag(int tagId, int adId);
}
