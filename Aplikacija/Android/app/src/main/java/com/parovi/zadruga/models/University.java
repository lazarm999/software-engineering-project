package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class University {
    @PrimaryKey
    public int id;
    public String name;
}
