package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.parovi.zadruga.models.entityModels.Message;

import java.util.List;

@Dao
public abstract class MessageDao extends BaseDao<Message> {

    @Query("SELECT * FROM Message " +
            "WHERE Message.qbChatId == :qbChatId")
    public abstract List<Message> getMessagesByQbChatId(String qbChatId);
}
