package com.parovi.zadruga.data;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.nonEntityModels.AdWithTags;

import java.text.DateFormat;
import java.util.List;

public class JobAdInfo {
    private long id;
    private String title;
    private String description;
    private String location;
    private String time;
    private float compensationFrom, compensationTo;
    private int noApplicantsRequired; // in minutes
    //private List<ApplicantResume> applicants;
    private User author;

    public JobAdInfo(Ad ad) {
        id = ad.getAdId();
        title = ad.getTitle();
        description = ad.getDescription();
        time = DateFormat.getDateInstance(DateFormat.MEDIUM).format(ad.getPostTime());
        compensationFrom = ad.getCompensationMin();
        compensationTo = ad.getCompensationMax();
        noApplicantsRequired = ad.getNumberOfEmployees();
        author = ad.getEmployer();
        location = ad.getLocation().getCityName();
    }

    public boolean isAuthor(int userId) {
        return userId == author.getUserId();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public float getCompensationFrom() {
        return compensationFrom;
    }

    public float getCompensationTo() {
        return compensationTo;
    }

    public int getNoApplicantsRequired() {
        return noApplicantsRequired;
    }

    public User getAuthor() {
        return author;
    }
}
