package com.parovi.zadruga.items;

import java.time.LocalDate;

public class JobItem  {
    private String title;
    private LocalDate date;
    private float rating;

    public JobItem()
    {

    }

    public JobItem(String title, LocalDate date, float rate)
    {
        this.title = title;
        this.date = date;
        this.rating = rate;
    }

    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title = title;}
    public LocalDate getDate() {return this.date;}
    public void setDate(LocalDate date) {this.date = date;}
    public float getRating() {return this.rating;}
    public void setRating(float rate) {this.rating = rate;}
}
