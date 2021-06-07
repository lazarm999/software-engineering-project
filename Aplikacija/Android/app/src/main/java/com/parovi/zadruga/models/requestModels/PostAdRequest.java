package com.parovi.zadruga.models.requestModels;

import java.util.Date;
import java.util.List;

public class PostAdRequest {
    private String title;
    private String description;
    private int compensationMin;
    private int compensationMax;
    private int numberOfEmployees;
    private Date postTime;
    private int locationId;
    private List<Integer> tags;

    public PostAdRequest(String title, String description, int compensationMin, int compensationMax, int numberOfEmployees, int locationId, List<Integer> tags) {
        this.title = title;
        this.description = description;
        this.compensationMin = compensationMin;
        this.compensationMax = compensationMax;
        this.numberOfEmployees = numberOfEmployees;
        this.locationId = locationId;
        this.tags = tags;
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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
}
