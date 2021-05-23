package com.parovi.zadruga.models.entityModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(tableName = "user_table",
        foreignKeys = @ForeignKey(entity = Faculty.class,
                            parentColumns = "facultyId",
                            childColumns = "fkFacultyId",
                            onDelete = SET_NULL))
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String bio;
    private String imageUrl;
    private String phoneNumber;
    private double avgRate;
    private String fcmToken;
    private String companyName;
    @ColumnInfo(index = true)
    private int fkFacultyId;
    private String type; //u Constants su zapamceni tipovi naloga
    private int qbId;
    private boolean isSynced;

    public User(int userId, String name){
        this.userId = userId;
        this.name = name;
    }

    public User(){
    }

    @Ignore
    public User(String username){
        this.username = username;
    }

    public User(String username, int qbId){
        this.username = username;
        this.qbId = qbId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(double avgRate) {
        this.avgRate = avgRate;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getFkFacultyId() {
        return fkFacultyId;
    }

    public void setFkFacultyId(int fkFacultyId) {
        this.fkFacultyId = fkFacultyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public int getQbId() {
        return qbId;
    }

    public void setQbId(int qbId) {
        this.qbId = qbId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
