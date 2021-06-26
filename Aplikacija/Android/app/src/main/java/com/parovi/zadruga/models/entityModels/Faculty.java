package com.parovi.zadruga.models.entityModels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = University.class,
        parentColumns = "universityId",
        childColumns = "fkUniversityId",
        onDelete = CASCADE))
public class Faculty {
    @PrimaryKey
    private int facultyId;
    private String name;
    @ColumnInfo(index = true)
    private int fkUniversityId;
    @Ignore
    private University university;

    public Faculty(int facultyId, String name, int fkUniversityId) {
        this.facultyId = facultyId;
        this.name = name;
        this.fkUniversityId = fkUniversityId;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return name + ", " + university.getName();
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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
