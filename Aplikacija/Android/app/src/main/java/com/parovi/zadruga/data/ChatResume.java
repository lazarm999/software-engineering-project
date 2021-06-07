package com.parovi.zadruga.data;

import java.time.LocalDateTime;

public class ChatResume {
    private long id;
    private String chatTitle;
    private String lastMessage;
    private LocalDateTime timeOfLastMessage;
    private boolean seen;

    public ChatResume(long id, String chatTitle, String lastMessage, LocalDateTime timeOfLastMessage) {
        this.id = id;
        this.chatTitle = chatTitle;
        this.lastMessage = lastMessage;
        this.timeOfLastMessage = timeOfLastMessage;
        this.seen = false;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getTimeOfLastMessage() {
        return timeOfLastMessage;
    }

    public long getId() {
        return id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
