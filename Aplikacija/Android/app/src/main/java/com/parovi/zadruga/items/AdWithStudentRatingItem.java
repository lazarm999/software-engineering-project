package com.parovi.zadruga.items;

import com.parovi.zadruga.R;

import java.time.LocalDate;

public class AdWithStudentRatingItem {
    protected long id;
    private String title;
    private LocalDate date;
    private int icRating;

    public AdWithStudentRatingItem()
    {

    }

    public AdWithStudentRatingItem(String title, LocalDate date)
    {
        //this.id = id;
        this.title = title;
        this.date = date;
        this.icRating = R.drawable.ic_users_grade;
    }

    public long getId() {return this.id;}
    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title = title;}
    public LocalDate getDate() {return this.date;}
    public void setDate(LocalDate date) {this.date = date;}
    public int getIcRating() {return this.icRating;}
    public void setIcRating(int img) {this.icRating = img;}

}
