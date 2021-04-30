package com.parovi.zadruga.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.User;
import com.parovi.zadruga.models.manyToManyModels.UserWithBadges;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_table")
    LiveData<List<UserWithBadges>> getAllUsersWithBadges();

    @Insert
    ListenableFuture<Long> insertUser(User user);
}
