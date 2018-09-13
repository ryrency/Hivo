package edu.sjsu.hivo.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import edu.sjsu.hivo.model.ListPropertyResponse;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.networking.VolleyNetwork;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private LatLng currentLocation;
    LocationManager locationManager;
    static final int TAG_CODE_PERMISSION_LOCATION = 110;
    private ArrayList<ListPropertyResponse> markerList = new ArrayList<>();
    String TAG = MapActivity.class.getSimpleName();
    TextView priceMarker;
    private IconGenerator iconGen;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_property_listing);
        sendRequestAndprintResponse("94560");
        priceMarker = findViewById(R.id.price_marker);
        iconGen = new IconGenerator(this);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.onCreate(null);
            mapFragment.onResume();
            mapFragment.getMapAsync(this);
        }




        //  map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkPermission();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"inside OnMapReady" );
        MapsInitializer.initialize(this);
        this.googleMap = googleMap;
        updateMap();




    }



    private void updateMap() {
        for(int i = 0; i< markerList.size(); i++){
            Log.i(TAG,"inside updateMap()" );
            if (googleMap != null && markerList.get(i) !=null) {
                currentLocation = new LatLng(markerList.get(i).getLatitude(), markerList.get(i).getLongitude() );
                Log.i(TAG,"location from property is" +currentLocation);
                createMarker(markerList.get(i).getLatitude(), markerList.get(i)
                        .getLongitude(), markerList.get(i).getPrice(),markerList.get(i).getAddress());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }

        }


            //checkPermission();
            //googleMap.setMyLocationEnabled(true);
        }


    protected Marker createMarker(double latitude, double longitude, String price, String address) {
        //iconGen.setColor(R.color.green);
        iconGen.setStyle(IconGenerator.STYLE_GREEN);
        iconGen.setTextAppearance(R.style.myStyleText);
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(price)
                .snippet(address)
                .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(getFormattedPrice(price))))
                .anchor(iconGen.getAnchorU(), iconGen.getAnchorV()));

    }

    private String getFormattedPrice(String price){
        String price1 = price.substring(1);
        int intPrice = Integer.parseInt(price1);
        Log.i(TAG,"intPrice " +intPrice);
        String makePrice = "";

        if(intPrice >= 1000 && intPrice < 1000000){
            intPrice = intPrice/1000;
            makePrice = String.valueOf(intPrice) +'k';
            Log.i(TAG,"makePrice value is " +makePrice);
        }

        if(intPrice >= 1000000 && intPrice < 1000000000){
            intPrice = intPrice/1000000;
            makePrice = String.valueOf(intPrice) +'M';
            Log.i(TAG,"makePrice value is " +makePrice);
        }

        return makePrice;


    }

    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocation);
        markerOptions.title("i'm here");
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));
    }


    private void addListObject(ListPropertyResponse listObject) {
        markerList.add(listObject);
        updateMap();
    }
    public void sendRequestAndprintResponse(final String zipcode) {
        checkPermission();
//        final JSONObject jsonResponse = new JSONObject();;
        Log.d(TAG,"inside sendRequestAndprintResponse()"+VolleyNetwork.AWS_ENDPOINT+"/dummy?zipcode="+zipcode);
        try{
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    VolleyNetwork.AWS_ENDPOINT+"/dummy?zipcode="+zipcode,
                    null,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response){
                            Log.d(TAG,"response is:" + response.toString());
                            ListPropertyResponse listPropertyResponse = null;
                            try {
                                for (int i = 0; i < response.length(); ++i) {
                                    JSONObject rec = response.getJSONObject(i);
                                    listPropertyResponse = ListPropertyResponse.fromJSONObjectResponse(rec);
                                    addListObject(listPropertyResponse);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.i(TAG,listPropertyResponse.getString(zipcode));
//                            Toast.makeText(MainActivity.this, "response: "+response, Toast.LENGTH_LONG).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error == null || error.networkResponse == null) {
                                return;
                            }

                            Log.e(TAG, "error making server request"+error.getMessage());
                            Toast.makeText(MapActivity.this, "error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );

            VolleyNetwork
                    .getInstance(getApplicationContext())
                    .getRequestQueue(this.getApplicationContext())
                    .add(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    TAG_CODE_PERMISSION_LOCATION);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}








