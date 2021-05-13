package com.parovi.zadruga.models;

import androidx.room.Entity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.type.DateTime;

public class Message {
    private int msgId;
    private Timestamp sentTime;
    private String text;

    public Message(Timestamp sentTime, String text) {
        this.sentTime = sentTime;
        this.text = text;
    }

    @Exclude
    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
