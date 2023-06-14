package com.example.eventmanager;

public class Activity {
    private String name;
    private String time;
    private String description;
    private String location;

    public Activity(String name, String time, String description, String location) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }
}

