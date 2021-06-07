package com.parovi.zadruga.models.requestModels;

public class BanRequest {
    private String banExplanation;

    public BanRequest(String banExplanation) {
        this.banExplanation = banExplanation;
    }

    public String getBanExplanation() {
        return banExplanation;
    }

    public void setBanExplanation(String banExplanation) {
        this.banExplanation = banExplanation;
    }
}
