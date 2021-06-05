package com.parovi.zadruga.data;

public class UserInfoResume {
    protected long id;
    protected String name;
    protected String username;

    public UserInfoResume(long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}

