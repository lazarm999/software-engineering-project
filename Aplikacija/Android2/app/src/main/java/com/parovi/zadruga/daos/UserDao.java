package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT Badge.badgeId, Badge.description " +
            "FROM UserBadge " +
            "INNER JOIN Badge ON UserBadge.fkBadgeId = Badge.badgeId " +
            "WHERE UserBadge.fkUserId = :userId")
    ListenableFuture<List<Badge>> getUserBadges(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertUsers(List<User> users);

    @Query("UPDATE user_table SET fcmToken = :fcmToken AND isSynced = :isSynced WHERE userId = :id")
    ListenableFuture<Integer> updateUserFcmToken(long id, String fcmToken, boolean isSynced);

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    ListenableFuture<User> getUserById(int userId);

    @Update
    ListenableFuture<Integer> updateUser(User user);
}
