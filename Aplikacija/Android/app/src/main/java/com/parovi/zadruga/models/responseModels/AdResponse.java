package com.parovi.zadruga.models.responseModels;

import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.User;

import java.util.Date;
import java.util.List;

public class AdResponse {
    private int adId;
    private String title;
    private String description;
    private int compensationMin;
    private int compensationMax;
    private int numberOfEmployees;
    private Date postTime;

    private User employer;
    private Location location;
    private List<Tag> tags;

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

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
