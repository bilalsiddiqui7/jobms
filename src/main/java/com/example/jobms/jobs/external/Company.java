package com.example.jobms.jobs.external;

import java.util.List;

public class Company {
    private long id;
    private String name;
    private String description;

    public Company(long id, String name, String description, List<Long> jobIds, List<Long> reviewIds) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

