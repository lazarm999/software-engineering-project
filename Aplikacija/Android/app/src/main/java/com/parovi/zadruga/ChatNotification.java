package com.parovi.zadruga;

import java.util.List;

public class ChatNotification {
    private String username;
    private String message;
    private Integer adId;
    private List<Integer> userQbIds;
    private String chatQbId;

    public ChatNotification(String username, String message, Integer adId, List<Integer> userQbIds, String chatQbId) {
        this.username = username;
        this.message = message;
        this.adId = adId;
        this.userQbIds = userQbIds;
        this.chatQbId = chatQbId;
    }

    public ChatNotification() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getUserQbIds() {
        return userQbIds;
    }

    public void setUserQbIds(List<Integer> userQbIds) {
        this.userQbIds = userQbIds;
    }

    public String getChatQbId() {
        return chatQbId;
    }

    public void setChatQbId(String chatQbId) {
        this.chatQbId = chatQbId;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }
}
