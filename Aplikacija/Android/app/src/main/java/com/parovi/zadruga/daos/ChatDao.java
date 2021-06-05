package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserChat;

import java.util.List;

@Dao
public interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertChat(Chat chat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertChats(List<Chat> chats);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertUserChat(UserChat userChat);

    @Query("SELECT Chat.* FROM UserChat " +
            "INNER JOIN Chat ON Chat.chatId LIKE UserChat.fkChatId " +
            "WHERE UserChat.fkUserId = :userId")
    ListenableFuture<List<Chat>> getAllChatsByUserId(int userId);
}
