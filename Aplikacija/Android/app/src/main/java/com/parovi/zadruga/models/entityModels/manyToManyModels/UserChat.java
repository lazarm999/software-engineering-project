package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkChatId", "fkQbUserId"},
        foreignKeys = {@ForeignKey(entity = Chat.class,
                        parentColumns = "chatId",
                        childColumns = "fkChatId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = User.class,
                        parentColumns = "userQbId",
                        childColumns = "fkQbUserId")})
public class UserChat {
    @ColumnInfo(index = true)
    @NonNull
    private String fkChatId;
    @ColumnInfo(index = true)
    private int fkQbUserId;

    public UserChat() {
    }

    public UserChat(String fkChatId, int fkQbUserId) {
        this.fkChatId = fkChatId;
        this.fkQbUserId = fkQbUserId;
    }

    public String getFkChatId() {
        return fkChatId;
    }

    public void setFkChatId(String fkChatId) {
        this.fkChatId = fkChatId;
    }

    public int getFkQbUserId() {
        return fkQbUserId;
    }

    public void setFkQbUserId(int fkQbUserId) {
        this.fkQbUserId = fkQbUserId;
    }
}
