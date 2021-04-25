package com.parovi.zadruga.models.oneToManyModels;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.parovi.zadruga.models.Faculty;
import com.parovi.zadruga.models.University;

import java.util.List;

public class FacultyOnUniversity {
    @Embedded
    public University u;
    @Relation(
            parentColumn = "id",
            entityColumn = "universityId"
    )
    public List<Faculty> faculties;
}
