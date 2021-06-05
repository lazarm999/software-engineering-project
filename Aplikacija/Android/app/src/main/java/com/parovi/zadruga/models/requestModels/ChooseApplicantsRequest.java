package com.parovi.zadruga.models.requestModels;

import java.util.List;

public class ChooseApplicantsRequest {
    private List<Integer> userIds;

    public ChooseApplicantsRequest(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
