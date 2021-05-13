package com.parovi.zadruga.models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Chat {

    private String chatId;
    private boolean isArchived;
    private List<Integer> memberIds;
    //private List<Message> messages;

    public Chat(String chatId, boolean isArchived, List<Integer> memberIds) {
        this.chatId = chatId;
        this.isArchived = isArchived;
        this.memberIds = memberIds;
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

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

}
