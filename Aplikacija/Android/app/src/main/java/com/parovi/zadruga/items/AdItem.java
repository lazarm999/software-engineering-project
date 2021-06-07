package com.parovi.zadruga.items;

import com.parovi.zadruga.R;

import java.time.LocalDate;

public class AdItem {
    private String title;
    private String desc;
    private LocalDate date;
    private int imgResource;

    public AdItem()
    {

    }

    public AdItem(String title, String desc, LocalDate date)
    {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.imgResource = R.drawable.ad_item;
    }

    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title = title;}
    public String getDesc() {return this.desc;}
    public void  setDesc(String desc) {this.desc = desc;}
    public LocalDate getDate() {return this.date;}
    public void setDate(LocalDate date) {this.date = date;}
    public int getImgResource() {return this.imgResource;}
    public void setImgResource(int img) {this.imgResource = img;}
}
