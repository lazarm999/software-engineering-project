package com.parovi.zadruga.items;

public class NotificationItem {
    private int imgResource;
    private String title;
    private String desc;

    public NotificationItem() {
    }

    public NotificationItem(int imgResource, String title, String desc) {
        this.imgResource = imgResource;
        this.title = title;
        this.desc = desc;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
