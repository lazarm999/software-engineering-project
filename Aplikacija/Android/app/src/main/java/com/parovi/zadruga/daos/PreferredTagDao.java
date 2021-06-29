package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.parovi.zadruga.models.entityModels.PreferredTag;

import java.util.List;

@Dao
public abstract class PreferredTagDao extends BaseDao<PreferredTag> {

    @Query("DELETE FROM PreferredTag WHERE PreferredTag.tagId = :tagId")
    public abstract Integer deletePreferredTag(int tagId);

    @Query("SELECT * FROM PreferredTag")
    public abstract List<PreferredTag> getPreferredTags();
}
