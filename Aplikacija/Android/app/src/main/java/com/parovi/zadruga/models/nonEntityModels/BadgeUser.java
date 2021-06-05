package com.parovi.zadruga.models.nonEntityModels;

import com.parovi.zadruga.models.entityModels.Badge;

public class BadgeUser {
    private int user;
    private Badge badge;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }
}
