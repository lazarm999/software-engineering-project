package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkChatId", "fkUserId"},
        foreignKeys = {@ForeignKey(entity = Chat.class,
                        parentColumns = "chatId",
                        childColumns = "fkChatId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = User.class,
                        parentColumns = "userQbId",
                        childColumns = "fkUserId")})
public class UserChat {
    @ColumnInfo(index = true)
    @NonNull
    private String fkChatId;
    @ColumnInfo(index = true)
    private int fkUserId;

    public UserChat() {
    }

    public UserChat(String fkChatId, int fkUserId) {
        this.fkChatId = fkChatId;
        this.fkUserId = fkUserId;
    }

    public String getFkChatId() {
        return fkChatId;
    }

    public void setFkChatId(String fkChatId) {
        this.fkChatId = fkChatId;
    }

    public int getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(int fkUserId) {
        this.fkUserId = fkUserId;
    }
}
