package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.SET_NULL;

/*(primaryKeys = {"firstName", "lastName"})
 @Ignore
 mogu da se nasledjuju entitiji
 @Fts4 - za Support full-text search
*/


@Entity(foreignKeys = {@ForeignKey(entity = Location.class,
                        parentColumns = "locId",
                        childColumns = "locationId",
                        onDelete = SET_NULL),
                        @ForeignKey(entity = Employer.class,
                        parentColumns = "userId",
                        childColumns = "fkEmployerId",
                        onDelete = SET_NULL)})
public class Ad {
    @PrimaryKey(autoGenerate = true)
    private int adId;
    private String title;
    private String desc;
    private int feeFrom;
    private int freeTo;
    private int peopleNeeded;
    private int locationId;
    private int fkEmployerId;

    public Ad(String title, String desc, int locationId){
        this.title = title;
        this.desc = desc;
        this.locationId = locationId;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public int getFkEmployerId() {
        return fkEmployerId;
    }

    public void setFkEmployerId(int fkEmployerId) {
        this.fkEmployerId = fkEmployerId;
    }
}
