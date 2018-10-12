package com.ensharp.kimyejin.voicerecognitiontest;

public class LocationVO {
    private static final LocationVO ourInstance = new LocationVO();

    public static LocationVO getInstance() {
        return ourInstance;
    }

    private double latitude;
    private double longitude;
    private int floor;

    private LocationVO() {
        latitude = 0;
        longitude = 0;
        floor = 0;
    }

    public void setLocation(double latitude, double longitude, int floor) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.floor = floor;
    }

    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public int getFloor() {return floor;}
}
