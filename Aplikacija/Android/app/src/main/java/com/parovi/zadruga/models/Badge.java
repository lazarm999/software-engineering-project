package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Badge {
    @PrimaryKey
    public int id;
    public String desc; //description
}
