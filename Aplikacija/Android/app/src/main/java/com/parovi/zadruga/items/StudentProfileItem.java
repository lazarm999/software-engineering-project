package com.parovi.zadruga.items;

import com.parovi.zadruga.models.Badge;

import java.util.List;

public class StudentProfileItem {
    private String name;
    private String surname;
    private String faculty;
    private String bio;
    private String phoneNum;
    private float rating;
    private List<Badge> badges;

    public StudentProfileItem() {

    }

    public StudentProfileItem(String name, String sname, String fac, String bio, String phone, float rate, List<Badge> badges) {
        this.name = name;
        this.surname = sname;
        this.faculty = fac;
        this.bio = bio;
        this.phoneNum = phone;
        this.rating = rate;
        this.badges = badges;
    }

    public String getName()
    {
        return name;
    }
    public String getSurname()
    {
        return surname;
    }
    public String getFaculty()
    {
        return faculty;
    }
    public String getBio()
    {
        return bio;
    }
    public List<Badge> getBadges()
    {
        return badges;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setSurname(String sname)
    {
        this.surname = sname;
    }
    public void setFaculty(String fac)
    {
        this.faculty = fac;
    }
    public void setBio(String bio)
    {
        this.bio = bio;
    }
    public void setBadges(List<Badge> badges)
    {
        this.badges = badges;
    }
}
