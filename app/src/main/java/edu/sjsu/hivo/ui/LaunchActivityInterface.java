package edu.sjsu.hivo.ui;


import android.content.Context;
import android.location.Location;

public interface LaunchActivityInterface {
    String checkResponse(String response,String zipcode);
    String getLatLonFromLocation(Location location,Context context);
}
