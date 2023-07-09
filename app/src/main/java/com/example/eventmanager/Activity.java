package com.example.eventmanager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class Activity implements Parcelable {
    private int id;
    private static int uniqueId;
    private String name;
    private String time;
    private String description;
    private String city;
    private String date;
    private String type;
    private int color;

    public Bitmap getImage1() {
        return image1;
    }

    public void setImage1(Bitmap image1) {
        this.image1 = image1;
    }

    public Bitmap getImage2() {
        return image2;
    }

    public void setImage2(Bitmap image2) {
        this.image2 = image2;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private Bitmap image1,image2;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Activity(String type, String name, String time, String description, String city, String date) {
        this.id=uniqueId;
        uniqueId++;
        this.type=type;
        this.name = name;
        this.time = time;
        this.description = description;
        this.city = city;
        this.date=date;
    }
    public Activity(int id,String type,String name, String time, String description,String city,String date) {
        this.id=id;
        this.type=type;
        this.name = name;
        this.time = time;
        this.description = description;
        this.city = city;
        this.date=date;
    }

    public Activity(int id,String type,String name, String time, String description,String city,String date,Bitmap image1,Bitmap image2,int color) {
        this.id=id;
        this.type=type;
        this.name = name;
        this.time = time;
        this.description = description;
        this.city = city;
        this.date=date;
        this.image1=image1;
        this.image2=image2;
        this.color=color;
    }

    protected Activity(Parcel in) {
        id=in.readInt();
        type=in.readString();
        name = in.readString();
        time = in.readString();
        description = in.readString();
        city=in.readString();
       // city.setName(in.readString());
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



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
       parcel.writeInt(id);
        parcel.writeString(type);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(time);
        parcel.writeString(city);
        parcel.writeString(date);

    }
}

