package com.parovi.zadruga.models.manyToManyModels;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Tag;
import com.parovi.zadruga.models.crossRefModels.AdTagCrossRef;

import java.util.List;

public class AdWithTags {
    @Embedded
    public Ad ad;
    @Relation(
            parentColumn = "id",
            entity = Tag.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = AdTagCrossRef.class,
                    parentColumn = "adId",
                    entityColumn = "tagId")
    )
    public List<Tag> tags;
}
