package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TmpPost {
    public String title;
    public String body;
    @PrimaryKey
    public int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
