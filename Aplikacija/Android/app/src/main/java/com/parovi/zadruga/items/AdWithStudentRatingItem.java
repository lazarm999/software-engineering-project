package com.parovi.zadruga.items;

import com.parovi.zadruga.R;

import java.time.LocalDate;

public class AdWithStudentRatingItem {
    protected long id;
    private String title;
    private LocalDate date;
    private float rating;
    private int icRating;

    public AdWithStudentRatingItem()
    {

    }

    public AdWithStudentRatingItem(long id, String title, LocalDate date, float rating)
    {
        this.id = id;
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.icRating = R.drawable.ic_users_grade;
    }

    public long getId() {return this.id;}
    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title = title;}
    public LocalDate getDate() {return this.date;}
    public void setDate(LocalDate date) {this.date = date;}
    public float getRating() {return this.rating;}
    public void setRating(float rating) {this.rating = rating;}
    public int getIcRating() {return this.icRating;}
    public void setIcRating(int img) {this.icRating = img;}

}
