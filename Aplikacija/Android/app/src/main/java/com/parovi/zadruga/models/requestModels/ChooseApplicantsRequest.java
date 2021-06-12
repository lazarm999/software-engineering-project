package com.parovi.zadruga.models.requestModels;

import java.util.List;

public class ChooseApplicantsRequest {
    private List<Integer> userIds;
    private String qbChatId;

    public ChooseApplicantsRequest(List<Integer> userIds, String qbChatId) {
        this.userIds = userIds;
        this.qbChatId = qbChatId;
    }

    public ChooseApplicantsRequest(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public String getQbChatId() {
        return qbChatId;
    }

    public void setQbChatId(String qbChatId) {
        this.qbChatId = qbChatId;
    }
}
