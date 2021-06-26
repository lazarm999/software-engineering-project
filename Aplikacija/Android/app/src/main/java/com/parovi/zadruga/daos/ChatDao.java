package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserChat;

import java.util.List;

@Dao
public abstract class ChatDao extends BaseDao<Chat> {

    @Query("SELECT Chat.* FROM UserChat " +
            "INNER JOIN Chat ON Chat.chatId LIKE UserChat.fkChatId " +
            "WHERE UserChat.fkQbUserId = :userId")
    public abstract List<Chat> getAllChatsByUserId(int userId);

    @Query("UPDATE Chat " +
            "SET chatTitle = :chatTitle "+
            "WHERE Chat.chatId LIKE :chatId")
    public abstract void updateChatTitle(String chatTitle, String chatId);

    @Query("UPDATE Chat " +
            "SET lastSenderName = :lastSenderName "+
            "WHERE Chat.chatId LIKE :chatId")
    public abstract void updateLastSenderName(String lastSenderName, String chatId);

    @Query("UPDATE Chat " +
            "SET lastMessage = :lastMessage "+
            "WHERE Chat.chatId LIKE :chatId")
    public abstract void updateLastMessage(String lastMessage, String chatId);

    @Query("UPDATE Chat " +
            "SET lastMessageDateSent = :lastMessageDateSent "+
            "WHERE Chat.chatId LIKE :chatId")
    public abstract void updateLastMessageDateSent(long lastMessageDateSent, String chatId);

    @Query("SELECT user_table.* FROM UserChat " +
            "INNER JOIN user_table ON user_table.userQbId = UserChat.fkQbUserId " +
            "WHERE UserChat.fkChatId LIKE :chatId")
    public abstract List<User> getChatMembers(String chatId);
}
