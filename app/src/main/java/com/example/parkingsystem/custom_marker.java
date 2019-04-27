package com.example.parkingsystem;

public class custom_marker {
    double latitude;
    double longitude;
    String spot_name;

    public custom_marker(double latitude, double longitude, String spot_name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.spot_name = spot_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSpot_name() {
        return spot_name;
    }

    public void setSpot_name(String spot_name) {
        this.spot_name = spot_name;
    }

}
