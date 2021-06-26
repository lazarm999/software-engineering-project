package com.parovi.zadruga.models.entityModels;

import android.graphics.Bitmap;

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
                        onDelete = CASCADE)})
public class Chat {
    @PrimaryKey
    @NonNull
    private String chatId;
    @ColumnInfo(index = true)
    @Nullable
    private Integer fkAdId;

    private Integer fkChatterId;
    private Utility.ChatType type;
    private String lastSenderName;
    private int numOfMembers;
    private String lastMessage;
    private long lastMessageDateSent;
    private Date createdAt;
    private boolean isArchived;
    private String chatTitle;

    @Ignore
    private QBChatDialog qbChat;

    @Ignore
    private Bitmap profileImage;

    public Chat(@NonNull String chatId, Utility.ChatType type, int numOfMembers, String lastMessage, long lastMessageDateSent, Date createdAt, QBChatDialog qbChat) {
        this.chatId = chatId;
        this.type = type;
        this.numOfMembers = numOfMembers;
        this.lastMessage = lastMessage;
        this.lastMessageDateSent = lastMessageDateSent;
        this.createdAt = createdAt;
        this.qbChat = qbChat;
    }

    public Chat() {
    }

    public Chat(@NonNull String chatId, Utility.ChatType type, String lastSenderName, int numOfMembers, String lastMessage, int fkLastSenderId,
                long lastMessageDateSent, Date createdAt, QBChatDialog qbChat) {
        this.chatId = chatId;
        this.type = type;
        this.lastSenderName = lastSenderName;
        this.numOfMembers = numOfMembers;
        this.lastMessage = lastMessage;
        this.lastMessageDateSent = lastMessageDateSent;
        this.createdAt = createdAt;
        this.qbChat = qbChat;
    }

    public Chat(@NonNull String chatId, Utility.ChatType type, String lastSenderName, int numOfMembers, Date createdAt) {
        this.chatId = chatId;
        this.type = type;
        this.lastSenderName = lastSenderName;
        this.numOfMembers = numOfMembers;
        this.lastMessage = lastMessage;
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

    public long getLastMessageDateSent() {
        return lastMessageDateSent;
    }

    public void setLastMessageDateSent(long lastMessageDateSent) {
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

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    @Nullable
    public Integer getFkChatterId() {
        return fkChatterId;
    }

    public void setFkChatterId(@Nullable Integer fkChatterId) {
        this.fkChatterId = fkChatterId;
    }

    /*public int getFkCreatorId() {
        return fkCreatorId;
    }

    public void setFkCreatorId(int fkCreatorId) {
        this.fkCreatorId = fkCreatorId;
    }*/
}
