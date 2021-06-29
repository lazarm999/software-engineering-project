package com.parovi.zadruga.models.entityModels;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Report {
    @PrimaryKey
    private int reportId;
    private String elaboration = "";
    private int fkReportedId;
    private int fkReporterId;
    private String adTitle = "";
    private String commentText = "";
    private int adId;

    @Ignore
    private User reported;
    @Ignore
    private User reporter;
    @Ignore
    private String type;

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getElaboration() {
        return elaboration;
    }

    public void setElaboration(String elaboration) {
        this.elaboration = elaboration;
    }

    public int getFkReportedId() {
        return fkReportedId;
    }

    public void setFkReportedId(int fkReportedId) {
        this.fkReportedId = fkReportedId;
    }

    public int getFkReporterId() {
        return fkReporterId;
    }

    public void setFkReporterId(int fkReporterId) {
        this.fkReporterId = fkReporterId;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public User getReported() {
        return reported;
    }

    public void setReported(User reported) {
        this.reported = reported;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
