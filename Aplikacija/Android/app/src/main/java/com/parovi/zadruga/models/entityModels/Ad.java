package com.parovi.zadruga.models.entityModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

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
                        onUpdate = CASCADE),
                        @ForeignKey(entity = Chat.class,
                        parentColumns = "chatId",
                        childColumns = "fkQbChatId",
                        onDelete = SET_NULL,
                        onUpdate = CASCADE)})
public class Ad {
    @PrimaryKey
    private int adId;
    private String title;
    private String description;
    private int compensationMin;
    private int compensationMax;
    private int numberOfEmployees;
    private Date postTime;
    @ColumnInfo(index = true)
    private int fkLocationId;
    @ColumnInfo(index = true)
    private int fkEmployerId;
    @ColumnInfo(index = true)
    private String fkQbChatId;

    public Ad(){

    }

    public Ad(int adId, String title, String description, int compensationMin, int compensationMax, int numberOfEmployees) {
        this.adId = adId;
        this.title = title;
        this.description = description;
        this.compensationMin = compensationMin;
        this.compensationMax = compensationMax;
        this.numberOfEmployees = numberOfEmployees;
    }

    public Ad(int adId, String title, String description, int compensationMin, int compensationMax, int numberOfEmployees, Date postTime) {
        this.adId = adId;
        this.title = title;
        this.description = description;
        this.compensationMin = compensationMin;
        this.compensationMax = compensationMax;
        this.numberOfEmployees = numberOfEmployees;
        this.postTime = postTime;
    }

    public Ad(String title, String description, int fkLocationId){
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCompensationMin() {
        return compensationMin;
    }

    public void setCompensationMin(int compensationMin) {
        this.compensationMin = compensationMin;
    }

    public int getCompensationMax() {
        return compensationMax;
    }

    public void setCompensationMax(int compensationMax) {
        this.compensationMax = compensationMax;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

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

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getFkQbChatId() {
        return fkQbChatId;
    }

    public void setFkQbChatId(String fkQbChatId) {
        this.fkQbChatId = fkQbChatId;
    }

   /* public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }



    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }*/
}
