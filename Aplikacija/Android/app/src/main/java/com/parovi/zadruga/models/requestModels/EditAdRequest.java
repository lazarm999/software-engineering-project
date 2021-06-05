package com.parovi.zadruga.models.requestModels;

import java.util.Date;
import java.util.List;

public class EditAdRequest {
    private String title;
    private String description;
    private int compensationMin;
    private int compensationMax;
    private int numberOfEmployees;
    private int locationId;
    private List<Integer> addTags;
    private List<Integer> removeTags;

    public EditAdRequest() {
    }

    public EditAdRequest(String title, String description, int compensationMin, int compensationMax, int numberOfEmployees, int locationId, List<Integer> addTags, List<Integer> removeTags) {
        this.title = title;
        this.description = description;
        this.compensationMin = compensationMin;
        this.compensationMax = compensationMax;
        this.numberOfEmployees = numberOfEmployees;
        this.locationId = locationId;
        this.addTags = addTags;
        this.removeTags = removeTags;
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

    public List<Integer> getAddTags() {
        return addTags;
    }

    public void setAddTags(List<Integer> addTags) {
        this.addTags = addTags;
    }

    public List<Integer> getRemoveTags() {
        return removeTags;
    }

    public void setRemoveTags(List<Integer> removeTags) {
        this.removeTags = removeTags;
    }
}
