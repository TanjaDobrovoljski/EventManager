package com.example.eventmanager;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Activity implements Parcelable {
    private String name;
    private String time;
    private String description;
    private String location;
    private String date;

    public Activity(String name, String time, String description, String location,String date) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.location = location;
        this.date=date;
    }

    protected Activity(Parcel in) {
        name = in.readString();
        time = in.readString();
        description = in.readString();
        location = in.readString();
        date = in.readString();
    }

    public static final Creator<Activity> CREATOR = new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel in) {
            return new Activity(in);
        }

        @Override
        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(time);
        parcel.writeString(location);
        parcel.writeString(date);
    }
}

