package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.parovi.zadruga.models.entityModels.Tagged;

import java.util.List;

@Dao
public abstract class TaggedDao extends BaseDao<Tagged> {

    @Query("SELECT Tagged.tagIndex FROM Tagged WHERE Tagged.fkCommentId == :commentId")
    public abstract List<Integer> getTaggedByCommentId(int commentId);
}
