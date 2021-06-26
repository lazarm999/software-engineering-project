package com.parovi.zadruga.models.entityModels;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Nullable;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(tableName = "user_table",
        foreignKeys = @ForeignKey(entity = Faculty.class,
                            parentColumns = "facultyId",
                            childColumns = "fkFacultyId",
                            onDelete = SET_NULL,
                            onUpdate = CASCADE),
        indices = {@Index(value = {"userQbId"}, unique = true)})
public class User {
    @PrimaryKey
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String bio;
    private String phoneNumber;
    private boolean isAdmin;
    private boolean isEmployer;
    private String companyName;
    private String fcmToken;
    @ColumnInfo(index = true)
    @Nullable
    @SerializedName("facultyId")
    private Integer fkFacultyId;
    private int userQbId;


    @Ignore
    private Faculty faculty;
    @Ignore
    private List<Badge> badges;
    @Ignore
    private Bitmap profileImage;

    public User(int userId, int userQbId) {
        this.userId = userId;
        this.userQbId = userQbId;
    }

    public User(int userId, String firstName){
        this.userId = userId;
        this.firstName = firstName;
    }

    public User(int userId, String username, @Nullable Integer fkFacultyId) {
        this.userId = userId;
        this.username = username;
        this.fkFacultyId = fkFacultyId;
    }

    public User(){
    }

    public User(String email, String password) {
        this.email = email;
    }

    public User(String email, String password, int userQbId) {
        this.email = email;
        this.userQbId = userQbId;
    }

    public User(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    @Ignore
    public User(String username){
        this.username = username;
    }

    public User(String username, int userQbId){
        this.username = username;
        this.userQbId = userQbId;
    }

    public boolean isEmployer() {
        return isEmployer;
    }

    public void setEmployer(boolean employer) {
        isEmployer = employer;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Integer getFkFacultyId() {
        return fkFacultyId;
    }

    public void setFkFacultyId(@Nullable Integer fkFacultyId) {
        this.fkFacultyId = fkFacultyId;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public int getUserQbId() {
        return userQbId;
    }

    public void setUserQbId(int userQbId) {
        this.userQbId = userQbId;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
        fkFacultyId = faculty.getFacultyId();
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
