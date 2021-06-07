package com.parovi.zadruga.models.entityModels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;
import com.parovi.zadruga.Utility;
import com.quickblox.chat.model.QBChatDialog;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = CASCADE),
                        /*@ForeignKey(entity = User.class,
                        parentColumns = "userQbId",
                        childColumns = "fkCreatorId",
                        onDelete = CASCADE),*/
                        @ForeignKey(entity = User.class,
                        parentColumns = "userQbId",
                        childColumns = "fkLastSenderId",
                        onDelete = CASCADE)})
public class Chat {
    @PrimaryKey
    @NonNull
    private String chatId;
    @ColumnInfo(index = true)
    @Nullable
    private Integer fkAdId;
    /*@ColumnInfo(index = true)
    private int fkCreatorId;*/
    private Utility.ChatType type;
    private String lastSenderName;
    private int numOfMembers;
    private String lastMessage;
    @ColumnInfo(index = true)
    private int fkLastSenderId;
    private Date lastMessageDateSent;
    private Date createdAt;
    private boolean isArchived;

    @Ignore
    private QBChatDialog qbChat;

    public Chat() {
    }

    public Chat(@NonNull String chatId, Utility.ChatType type, String lastSenderName, int numOfMembers, Date createdAt) {
        this.chatId = chatId;
        //this.fkCreatorId = fkCreatorId;
        this.type = type;
        this.lastSenderName = lastSenderName;
        this.numOfMembers = numOfMembers;
        this.lastMessage = lastMessage;
        this.fkLastSenderId = fkLastSenderId;
        this.lastMessageDateSent = lastMessageDateSent;
        this.createdAt = createdAt;
    }

    public Chat(String chatId, boolean isArchived, List<Integer> memberIds) {
        this.chatId = chatId;
        this.isArchived = isArchived;
    }

    @Exclude
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public Integer getFkAdId() {
        return fkAdId;
    }

    public void setFkAdId(@Nullable Integer fkAdId) {
        this.fkAdId = fkAdId;
    }

    public Utility.ChatType getType() {
        return type;
    }

    public void setType(Utility.ChatType type) {
        this.type = type;
    }

    public String getLastSenderName() {
        return lastSenderName;
    }

    public void setLastSenderName(String lastSenderName) {
        this.lastSenderName = lastSenderName;
    }

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public void setNumOfMembers(int numOfMembers) {
        this.numOfMembers = numOfMembers;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getFkLastSenderId() {
        return fkLastSenderId;
    }

    public void setFkLastSenderId(int fkLastSenderId) {
        this.fkLastSenderId = fkLastSenderId;
    }

    public Date getLastMessageDateSent() {
        return lastMessageDateSent;
    }

    public void setLastMessageDateSent(Date lastMessageDateSent) {
        this.lastMessageDateSent = lastMessageDateSent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public QBChatDialog getQbChat() {
        return qbChat;
    }

    public void setQbChat(QBChatDialog qbChat) {
        this.qbChat = qbChat;
    }

    /*public int getFkCreatorId() {
        return fkCreatorId;
    }

    public void setFkCreatorId(int fkCreatorId) {
        this.fkCreatorId = fkCreatorId;
    }*/
}