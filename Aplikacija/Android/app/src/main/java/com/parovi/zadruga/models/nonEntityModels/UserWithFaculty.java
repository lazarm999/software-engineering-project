package com.parovi.zadruga.models.nonEntityModels;

import androidx.room.Embedded;

import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.University;
import com.parovi.zadruga.models.entityModels.User;

public class UserWithFaculty {
    @Embedded
    private User user;
    @Embedded
    private Faculty faculty;
    @Embedded(prefix = "u_")
    private University university;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
