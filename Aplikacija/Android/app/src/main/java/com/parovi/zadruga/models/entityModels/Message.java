package com.parovi.zadruga.models.entityModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = {@ForeignKey(
                        entity = User.class,
                        parentColumns = "userQbId",
                        childColumns = "fkSenderId",
                        onDelete = CASCADE),
                        @ForeignKey(
                        entity = Chat.class,
                        parentColumns = "chatId",
                        childColumns = "qbChatId",
                        onDelete = CASCADE)})
public class Message {
    @PrimaryKey
    @NonNull
    private String msgId;
    private int fkSenderId;
    private Date dateSent;
    private String message;
    private String qbChatId;

    public Message() {
    }

    public Message(@NonNull String msgId, int fkSenderId, Date dateSent, String message, String qbChatId) {
        this.msgId = msgId;
        this.fkSenderId = fkSenderId;
        this.dateSent = dateSent;
        this.message = message;
        this.qbChatId = qbChatId;
    }

    public Message(Timestamp sentTime, String message) {
        this.message = message;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFkSenderId() {
        return fkSenderId;
    }

    public void setFkSenderId(int fkSenderId) {
        this.fkSenderId = fkSenderId;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getQbChatId() {
        return qbChatId;
    }

    public void setQbChatId(String qbChatId) {
        this.qbChatId = qbChatId;
    }
}