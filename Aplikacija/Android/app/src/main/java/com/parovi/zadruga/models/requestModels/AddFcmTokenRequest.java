package com.parovi.zadruga.models.requestModels;

public class AddFcmTokenRequest {
    private String oldFcmToken;
    private String fcmToken;

    public AddFcmTokenRequest(String oldFcmToken, String fcmToken) {
        this.oldFcmToken = oldFcmToken;
        this.fcmToken = fcmToken;
    }

    public AddFcmTokenRequest(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getOldFcmToken() {
        return oldFcmToken;
    }

    public void setOldFcmToken(String oldFcmToken) {
        this.oldFcmToken = oldFcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
