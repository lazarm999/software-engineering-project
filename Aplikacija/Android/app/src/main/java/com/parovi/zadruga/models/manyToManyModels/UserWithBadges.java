package com.parovi.zadruga.models.manyToManyModels;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.parovi.zadruga.models.Badge;
import com.parovi.zadruga.models.User;
import com.parovi.zadruga.models.crossRefModels.UserBadgeCrossRef;

import java.util.List;

public class UserWithBadges {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entity = Badge.class,
            entityColumn = "badgeId",
            associateBy = @Junction(
                    value = UserBadgeCrossRef.class,
                    parentColumn = "fkUserId",
                    entityColumn = "fkBadgeId"))
    public List<Badge> badges;
}
