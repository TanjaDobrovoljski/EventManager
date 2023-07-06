package com.example.eventmanager;

public class City {
    private String name;
    private int id;
    private static int uniqueId;
    private double x;
    private double y;

    public City(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.id=uniqueId++;
    }
    public City(int id,String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.id=id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }


}
