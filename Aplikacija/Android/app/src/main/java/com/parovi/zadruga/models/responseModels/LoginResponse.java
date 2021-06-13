package com.parovi.zadruga.models.responseModels;

import com.parovi.zadruga.models.entityModels.User;

public class LoginResponse {
    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
