package com.parovi.zadruga.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

/*(primaryKeys = {"firstName", "lastName"})
 @Ignore
 mogu da se nasledjuju entitiji
 @Fts4 - za Support full-text search
*/


@Entity(foreignKeys = {@ForeignKey(entity = Location.class,
                        parentColumns = "locId",
                        childColumns = "fkLocationId",
                        onDelete = SET_NULL),
                        @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkEmployerId",
                        onDelete = SET_NULL,
                        onUpdate = CASCADE)})
public class Ad {
    @PrimaryKey(autoGenerate = true)
    private int adId;
    private String title;
    private String desc;
    private int feeFrom;
    private int freeTo;
    private int peopleNeeded;
    //private Date date;
    //private boolean isClosed;
    @ColumnInfo(index = true)
    private int fkLocationId;
    @ColumnInfo(index = true)
    private int fkEmployerId;
    private int isSynced;

    public Ad(String title, String desc, int fkLocationId){
        this.title = title;
        this.desc = desc;
        this.fkLocationId = fkLocationId;
        this.fkEmployerId = 1;
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

    public void setFreeTo(int freeTo) { this.freeTo = freeTo; }

    public int getPeopleNeeded() {
        return peopleNeeded;
    }

    public void setPeopleNeeded(int peopleNeeded) {
        this.peopleNeeded = peopleNeeded;
    }

    //public Date getDate() { return date; }

    //public void setDate(Date date) { this.date = date; }

    public int getFkLocationId() {
        return fkLocationId;
    }

    public void setFkLocationId(int fkLocationId) {
        this.fkLocationId = fkLocationId;
    }

    public int getFkEmployerId() {
        return fkEmployerId;
    }

    public void setFkEmployerId(int fkEmployerId) {
        this.fkEmployerId = fkEmployerId;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }
}
