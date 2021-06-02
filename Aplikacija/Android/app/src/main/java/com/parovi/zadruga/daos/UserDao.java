package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.University;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserBadge;
import com.parovi.zadruga.models.nonEntityModels.UserWithFaculty;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT user_table.*, Faculty.*, University.universityId as u_universityId, University.name as u_name " +
            "FROM user_table " +
            "LEFT JOIN Faculty ON user_table.fkFacultyId = Faculty.facultyId " +
            "LEFT JOIN University ON Faculty.fkUniversityId = University.universityId " +
            "WHERE user_table.userId = :userId")
    ListenableFuture<UserWithFaculty> getUserWithFaculty(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertUsers(List<User> users);

    @Query("UPDATE user_table SET fcmToken = :fcmToken WHERE userId = :id")
    ListenableFuture<Integer> updateUserFcmToken(long id, String fcmToken);

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    ListenableFuture<User> getUserById(int userId);

    @Query("SELECT user_table.* FROM Applied " +
            "INNER JOIN user_table ON user_table.userId = Applied.fkUserId " +
            "WHERE fkAdId = :adId")
    ListenableFuture<List<User>> getAppliedUsers(int adId);

    @Update
    ListenableFuture<Integer> updateUser(User user);
}
