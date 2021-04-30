package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = @ForeignKey(entity = University.class,
        parentColumns = "universityId",
        childColumns = "fkUniversityId",
        onDelete = CASCADE))
public class Faculty {
    @PrimaryKey(autoGenerate = true)
    private int facultyId;
    private String name;
    private int fkUniversityId;

    public Faculty(String name, int fkUniversityId) {
        this.name = name;
        this.fkUniversityId = fkUniversityId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFkUniversityId() {
        return fkUniversityId;
    }

    public void setFkUniversityId(int fkUniversityId) {
        this.fkUniversityId = fkUniversityId;
    }

}
