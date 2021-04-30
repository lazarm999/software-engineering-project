package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employer extends User {
    private String companyName;

    public Employer(String name, String companyName){
        super(name);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
