package com.parovi.zadruga.items;

import java.time.LocalDate;

public class AchievementItem {

    private String title;
    private LocalDate date;
    private float rating;
    private String comment;

    public AchievementItem()
    {

    }

    public AchievementItem(String title, LocalDate date, float rate, String comment) {
        this.title = title;
        this.date = date;
        this.rating = rate;
        this.comment = comment;
    }

    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title = title;}
    public LocalDate getDate() {return this.date;}
    public void setDate(LocalDate date) {this.date = date;}
    public float getRating() {return this.rating;}
    public void setRating(float rate) {this.rating = rate;}
    public String getComment() {return this.comment;}
    public void setComment(String comment) {this.comment = comment;}
}
