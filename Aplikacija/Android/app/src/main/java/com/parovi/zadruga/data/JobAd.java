package com.parovi.zadruga.data;

import java.time.LocalDate;
import java.util.List;

public class JobAd {
    private long id;
    private String title;
    private String description;
    private String location;
    private LocalDate date;
    private float compensationFrom, compensationTo;
    private int duration; // in minutes
    private List<ApplicantResume> applicants;
    private boolean mine;

    public JobAd(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getCompensationFrom() {
        return compensationFrom;
    }

    public void setCompensationFrom(float compensationFrom) {
        this.compensationFrom = compensationFrom;
    }

    public float getCompensationTo() {
        return compensationTo;
    }

    public void setCompensationTo(float compensationTo) {
        this.compensationTo = compensationTo;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<ApplicantResume> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<ApplicantResume> applicants) {
        this.applicants = applicants;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
}
