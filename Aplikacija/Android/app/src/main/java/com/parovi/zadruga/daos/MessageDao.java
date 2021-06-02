package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.nonEntityModels.CommentWithUser;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM Message " +
            "WHERE Message.qbChatId == :qbChatId")
    ListenableFuture<List<Message>> getMessagesByQbChatId(int qbChatId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertMessages(List<Message> messages);
}
