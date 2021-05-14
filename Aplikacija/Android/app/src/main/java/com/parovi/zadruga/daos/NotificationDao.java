package com.parovi.zadruga.daos;


import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.Notification;
import com.parovi.zadruga.models.manyToManyModels.Comment;

import java.util.List;

public interface NotificationDao {
    /*TODO: ovde treba da ide neka dobudzenija/prostija notifikacija*/
    @Query("SELECT * FROM Notification ")
    LiveData<List<Notification>> getNotificationsByReceiverId(int receiverId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertNotification(Notification notification);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertNotifications(List<Notification> notifications);
}
