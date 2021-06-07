package com.parovi.zadruga.items;

public class NotificationItem {
    private int imgResource;
    private String title;
    private String desc;
    private String type;

    public NotificationItem() {
    }

    public NotificationItem(int imgResource, String title, String desc, String type) {
        this.imgResource = imgResource;
        this.title = title;
        this.desc = desc;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
