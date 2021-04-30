package com.parovi.zadruga.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = @ForeignKey(entity = Faculty.class,
        parentColumns = "facultyId",
        childColumns = "fkFacultyId",
        onDelete = SET_NULL))
public class Employee extends User {
    private int fkFacultyId;

    public Employee(String name, int fkFacultyId) {
        super(name);
        this.fkFacultyId = fkFacultyId;
    }

    public int getFkFacultyId() {
        return fkFacultyId;
    }

    public void setFkFacultyId(int fkFacultyId) {
        this.fkFacultyId = fkFacultyId;
    }
}
