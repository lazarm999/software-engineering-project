package com.parovi.zadruga.models.responseModels;

public class LoginResponse {
    private int id;
    private String token;
    private int qbId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getQbId() {
        return qbId;
    }

    public void setQbId(int qbId) {
        this.qbId = qbId;
    }
}
