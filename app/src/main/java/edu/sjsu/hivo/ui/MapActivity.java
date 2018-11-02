package edu.sjsu.hivo.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.sjsu.hivo.model.Property;

import com.android.volley.DefaultRetryPolicy;
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
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.networking.VolleyNetwork;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetail;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap googleMap;
    private LatLng currentLocation;
    LocationManager locationManager;
    private static final int TAG_CODE_PERMISSION_LOCATION = 110;
    private ArrayList<Property> propertyList = new ArrayList<>();
    String TAG = MapActivity.class.getSimpleName();
    private TextView mapTextView;
    private ImageView mapImageView;
    private IconGenerator iconGen;
    private Gson gson = new Gson();
    Marker myMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.map_property_listing);
        sendRequestAndprintResponse("95126");
        mapTextView = (TextView)findViewById(R.id.list_map_tv);
        mapImageView = (ImageView)findViewById(R.id.list_map_iv);
        moveToListVew();
        iconGen = new IconGenerator(this);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.onCreate(null);
            mapFragment.onResume();
            mapFragment.getMapAsync(this);
        }
//        checkPermission();
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"inside OnMapReady" );
        MapsInitializer.initialize(this);
        this.googleMap = googleMap;
        updateMap();

    }



    private void updateMap() {
        for(int i = 0; i< propertyList.size(); i++){
            Log.i(TAG,"inside updateMap()" );
            if(googleMap != null)
            if (googleMap != null && propertyList.get(i) !=null && propertyList.get(i).isLocationAvailable()) {
                currentLocation = new LatLng(propertyList.get(i).getLatitude(), propertyList.get(i).getLongitude() );
                Log.i(TAG,"location from property is" +currentLocation);
                myMarker = createMarker(propertyList.get(i).getLatitude(), propertyList.get(i)
                        .getLongitude(), propertyList.get(i).getPrice(),
                        propertyList.get(i).getAddress(), propertyList.get(i).getSaleType());
                myMarker.setTag(propertyList.get(i));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                googleMap.setOnMarkerClickListener(this);

            }
        }
        }


    protected Marker createMarker(double latitude, double longitude, String price, String address, String saleType) {
        if(saleType.equals("Sold")) {
            iconGen.setStyle(IconGenerator.STYLE_RED);
        }
        else {
            iconGen.setStyle(IconGenerator.STYLE_GREEN);
        }

        iconGen.setTextAppearance(R.style.myStyleText);
        myMarker =  googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(price)
                .snippet(address)
                .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(getFormattedPrice(price))))
                .anchor(iconGen.getAnchorU(), iconGen.getAnchorV()));

        return myMarker;

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        //if (marker.equals(myMarker))
        //{
            gotToDetailPageWhenClicked(marker);
        //}

        return true;
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

    private void gotToDetailPageWhenClicked(Marker myMarker){

        Intent intent = new Intent(this, PropertyDetail.class);
        intent.putExtra("JSONClass", gson.toJson(myMarker.getTag()));
        intent.setClass(this,PropertyDetail.class);
        this.startActivity(intent);

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


    private void addListObject(Property listObject) {
        propertyList.add(listObject);
        updateMap();
    }
    public void sendRequestAndprintResponse(final String zipcode) {
        checkPermission();
        try{
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    VolleyNetwork.AWS_ENDPOINT+"/cordinate?longitude=-122.0305563&latitude=37.3224014",
                    null,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response){
                            Log.d(TAG,"response is:" + response.toString());
                            Type listType = new TypeToken<ArrayList<Property>>(){}.getType();
                            List<Property> list = new Gson().fromJson(response.toString(), listType);
                            propertyList.addAll(list);
                            updateMap();
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
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                        PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                    TAG_CODE_PERMISSION_LOCATION);
        }

    }

    private void moveToListVew() {
        mapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            }
        });
        mapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            }
        });


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








