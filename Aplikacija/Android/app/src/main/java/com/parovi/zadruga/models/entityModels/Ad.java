package com.parovi.zadruga.models.entityModels;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
public class Ad {
    @PrimaryKey
    private int adId;
    private String title;
    private String description;
    private int compensationMin;
    private int compensationMax;
    private int numberOfEmployees;
    private int numberOfApplied;
    private Date postTime;
    @ColumnInfo(index = true)
    private int fkLocationId;
    @ColumnInfo(index = true)
    private int fkEmployerId;
    @SerializedName("qbChatId")
    private String fkQbChatId;

    private boolean isClosed;
    @Ignore
    private List<Tag> tags;
    @Ignore
    private User employer;
    @Ignore
    private Location location;
    @Ignore
    private Bitmap employerProfileImage;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Ad(int adId, String title, String description, int compensationMin, int compensationMax, int numberOfEmployees, LocalDate postTime, int fkEmployerId) {
        this.adId = adId;
        this.title = title;
        this.description = description;
        this.compensationMin = compensationMin;
        this.compensationMax = compensationMax;
        this.numberOfEmployees = numberOfEmployees;
        this.postTime = java.util.Date.from(postTime.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        this.fkEmployerId = fkEmployerId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Ad(String title, String description, LocalDate date)
    {
        this.title = title;
        this.description = description;
        this.postTime = java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public Ad(String title, String description, int fkLocationId){
        this.title = title;
        this.description = description;
        this.fkLocationId = fkLocationId;
        this.fkEmployerId = 1;
    }

    public int getNumberOfApplied() {
        return numberOfApplied;
    }

    public void setNumberOfApplied(int numberofApplied) {
        this.numberOfApplied = numberofApplied;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getEmployer() {
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

    public Bitmap getEmployerProfileImage() {
        return employerProfileImage;
    }

    public void setEmployerProfileImage(Bitmap employerProfileImage) {
        this.employerProfileImage = employerProfileImage;
    }
}
