package com.parovi.zadruga.data;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public List<Long> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(List<Long> jobTypes) {
        this.jobTypes = jobTypes;
    }

    private String phoneNo;
    private List<Long> jobTypes;

    public UserInfo(long id, String username) {
        this.id = id;
        this.username = username;
        this.jobTypes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
