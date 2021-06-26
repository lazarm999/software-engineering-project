package com.parovi.zadruga.models.requestModels;

import java.util.List;

public class ChatMembersRequest {
    private List<Integer> userQbIds;

    public ChatMembersRequest() {
    }

    public ChatMembersRequest(List<Integer> userQbIds) {
        this.userQbIds = userQbIds;
    }

    public List<Integer> getUserQbIds() {
        return userQbIds;
    }

    public void setUserQbIds(List<Integer> userQbIds) {
        this.userQbIds = userQbIds;
    }
}
