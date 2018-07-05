package com.skt.tmaphot.location;

public class GPSData {
    public static double latitude;
    public static double longitude;

    public GPSData(double latitude_, double longitude_) {
        this.latitude = latitude_;
        this.longitude = longitude_;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
