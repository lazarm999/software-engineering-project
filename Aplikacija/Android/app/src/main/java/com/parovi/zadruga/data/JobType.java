package com.parovi.zadruga.data;

import java.util.ArrayList;
import java.util.List;

public class JobType {
    private long id;
    private String title;
    private String description;

    private JobType(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    private static List<JobType> JOB_TYPES;

    public static List<JobType>getJobTypes() {
        if (JOB_TYPES == null) {
            JOB_TYPES = new ArrayList<>();
            JobType.loadJobTypes();
        }
        return JOB_TYPES;
    }

    static private void loadJobTypes() {
        for (int i = 1; i <= 10; i++) {
            JOB_TYPES.add(new JobType(i,
                    "JobType " + i,
                    "Description " + i));
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
