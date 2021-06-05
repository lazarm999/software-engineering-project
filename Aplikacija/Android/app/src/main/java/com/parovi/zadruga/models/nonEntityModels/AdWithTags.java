package com.parovi.zadruga.models.nonEntityModels;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;

import java.util.ArrayList;
import java.util.List;

public class AdWithTags {
    @Embedded
    public AdEmployerLocation adEmployerLocation;
    @Relation(
            parentColumn = "adId",
            entityColumn = "tagId",
            associateBy = @Junction(value = AdTag.class,
                    parentColumn = "fkAdId",
                    entityColumn = "fkTagId")
    )
    public List<Tag> tags;

    public AdWithTags() {
        adEmployerLocation = new AdEmployerLocation();
        tags = new ArrayList<>();
    }
}
