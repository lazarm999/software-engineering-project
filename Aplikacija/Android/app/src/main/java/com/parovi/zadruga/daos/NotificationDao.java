package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.parovi.zadruga.models.entityModels.Notification;

import java.util.List;

@Dao
public abstract class NotificationDao extends BaseDao<Notification> {
    @Query("SELECT * FROM Notification " +
            "LEFT JOIN Ad ON Notification.fkAdId = Ad.adId " +
            //"LEFT JOIN Comment ON Notification.fkCommentId = Comment.commentId " +
            //"LEFT JOIN Rating ON Notification.fkRatingId = Rating.ratingId " +
            "order by Notification.postTime desc")
    public abstract List<Notification> getNotifications();
}
