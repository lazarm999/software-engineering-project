package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Faculty {
    @PrimaryKey
    public int id;
    public String naziv;
    public int universityId;
}
