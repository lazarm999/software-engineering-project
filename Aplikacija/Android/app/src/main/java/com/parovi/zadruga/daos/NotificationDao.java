package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.parovi.zadruga.models.entityModels.Notification;

import java.util.List;

@Dao
public abstract class NotificationDao extends BaseDao<Notification> {

    @Query("SELECT * FROM Notification " +
            "order by Notification.postTime desc")
    public abstract List<Notification> getNotifications();
}
