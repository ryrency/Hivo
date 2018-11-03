package edu.sjsu.hivo.ui;

import android.util.Log;

import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LaunchActivityImpl implements LaunchActivityInterface {

    String extension ="";


    public String checkResponse(String response){
        if (!response.equals("")) {
            if (response.matches("[0-9]+")) {

                extension = "/zdata?zipcode=" + response;
            } else {
                try {
                    response = URLEncoder.encode(response, "UTF-8");
                    extension = "/data?str=" + response;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        else
            extension="/zdata?zipcode=95126";
        return extension;
    }
}
