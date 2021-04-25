package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*(primaryKeys = {"firstName", "lastName"})
 @Ignore
 mogu da se nasledjuju entitiji
 @Fts4 - za Support full-text search
* */


@Entity
public class Ad {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String desc;
    private int feeFrom;
    private int freeTo;
    private int peopleNeeded;
    private int locationId;

    public Ad(String title, String desc, int locationId){
        this.title = title;
        this.desc = desc;
        this.locationId = locationId;
    }

    public int getFeeFrom() {
        return feeFrom;
    }

    public void setFeeFrom(int feeFrom) {
        this.feeFrom = feeFrom;
    }

    public int getFreeTo() {
        return freeTo;
    }

    public void setFreeTo(int freeTo) {
        this.freeTo = freeTo;
    }

    public int getPeopleNeeded() {
        return peopleNeeded;
    }

    public void setPeopleNeeded(int peopleNeeded) {
        this.peopleNeeded = peopleNeeded;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
