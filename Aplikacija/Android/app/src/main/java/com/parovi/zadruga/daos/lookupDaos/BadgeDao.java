package com.parovi.zadruga.daos.lookupDaos;

import androidx.room.Dao;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.daos.BaseDao;
import com.parovi.zadruga.models.entityModels.Badge;

import java.util.List;

@Dao
public abstract class BadgeDao extends BaseDao<Badge> {

    @Query("SELECT Badge.* " +
            "FROM UserBadge " +
            "INNER JOIN Badge ON UserBadge.fkBadgeId = Badge.badgeId " +
            "WHERE UserBadge.fkUserId = :userId")
    public abstract ListenableFuture<List<Badge>> getUserBadges(int userId);


}
