package com.parovi.zadruga.models.requestModels;

public class ReportRequest {
    private Integer adId;
    private Integer commentId;
    private String elaboration;

    public ReportRequest(Integer adId, String elaboration) {
        this.adId = adId;
        this.elaboration = elaboration;
    }

    public ReportRequest(String elaboration, Integer commentId) {
        this.commentId = commentId;
        this.elaboration = elaboration;
    }
}
