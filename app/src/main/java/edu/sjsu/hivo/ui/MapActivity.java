package edu.sjsu.hivo.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.sjsu.hivo.events.DetailActivityData;
import edu.sjsu.hivo.model.Property;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.ui.IconGenerator;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.networking.VolleyNetwork;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetail;
import edu.sjsu.hivo.ui.utility.FilterUtility;
import edu.sjsu.hivo.ui.utility.SortUtility;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraIdleListener {
    private GoogleMap googleMap;
    private LatLng currentLocation;
    LocationManager locationManager;
    private static final int TAG_CODE_PERMISSION_LOCATION = 110;
    private List<Property> propertyList = new ArrayList<>();
    String TAG = MapActivity.class.getSimpleName();
    private TextView mapTextView;
    private ImageView mapImageView;
    private IconGenerator iconGen;
    private Gson gson = new Gson();
    Marker myMarker;
    private EditText userInput;
    private ImageView enter;
    private String response;
    private ImageView filterImg,sortImg;
    private TextView filterText,sortText;
    private ImageView enterButton;
    private FilterUtility filterUtility;
    private SortUtility sortUtility;
    private String extension;
    String zipcode="95126";
    static final int PICK_SORT_REQUEST = 2;  // The request code
    static final int PICK_FILTER_REQUEST = 1;  // The request code
    LaunchActivityInterface launchActivityInterface;
    private FusedLocationProviderClient mFusedLocationClient;




    //private String extension;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_property_listing);
        checkPermission();

        launchActivityInterface = new LaunchActivityImpl();

        mapTextView = (TextView)findViewById(R.id.list_map_tv);
        mapImageView = (ImageView)findViewById(R.id.list_map_iv);
        userInput = (EditText)findViewById(R.id.enter_location);
        filterImg = (ImageView)findViewById(R.id.list_filter_iv);
        filterText = (TextView)findViewById(R.id.list_filter_tv);
        enterButton =  findViewById(R.id.enter_button);

        sortImg = findViewById(R.id.list_sort_iv);
        sortText = findViewById(R.id.list_sort_tv);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            zipcode = launchActivityInterface.getLatLonFromLocation(location,getApplicationContext());
                            sendRequestAndprintResponse("/zdata?zipcode="+zipcode);
                        }
                    }
                });

        filterUtility = new FilterUtility(this);
        filterUtility.setFilterListener(filterImg,filterText);

        sortUtility = new SortUtility(this);
        sortUtility.setSortListener(sortImg,sortText);

        moveToListVew();
        iconGen = new IconGenerator(this);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.onCreate(null);
            mapFragment.onResume();
            mapFragment.getMapAsync(this);
        }

        setEnterButtonListener();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"inside OnMapReady" );
        MapsInitializer.initialize(this);
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.3363447,-121.8811573), 12));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraIdleListener(this);
    }

    private void setEnterButtonListener(){
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response = userInput.getText().toString();
                userInput.getText().clear();
                userInput.clearFocus();

                extension=launchActivityInterface.checkResponse(response,zipcode);
                sendRequestAndprintResponse(extension);

            }
        });
    }
    private void updateMap() {
        Log.i(TAG,"inside updateMap()" );
        if (googleMap == null) return;

        for(int i = 0; i< propertyList.size(); i++){
            if (propertyList.get(i).isLocationAvailable()) {
                myMarker = createMarker(propertyList.get(i).getLatitude(), propertyList.get(i)
                                .getLongitude(), propertyList.get(i).getPrice(),
                        propertyList.get(i).getAddress(), propertyList.get(i).getSaleType());
                myMarker.setTag(propertyList.get(i));
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
        if(!price.equals("Not known")){
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
        return "";
    }

    private void gotToDetailPageWhenClicked(Marker marker){

        Intent intent = new Intent(this, PropertyDetail.class);
        DetailActivityData detailActivityData = new DetailActivityData((Property) marker.getTag());
        EventBus.getDefault().postSticky(detailActivityData);
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

    public void sendRequestAndprintResponse(String extension) {
        Log.d(TAG, "Fetching data");
        try{
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    VolleyNetwork.AWS_ENDPOINT+extension,
                    null,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response){
                            Log.d(TAG,"response is:" + response.toString());
                            Type listType = new TypeToken<ArrayList<Property>>(){}.getType();
                            List<Property> list = new Gson().fromJson(response.toString(), listType);
                            propertyList.clear();
                            propertyList.addAll(list);
                            if (propertyList.size() >  1)
                                updateMap();
                            else if (propertyList.size() ==  1){
                                Log.d("RENCY", "Into size==1");
                                DetailActivityData detailActivityData = new DetailActivityData(propertyList.get(0));
                                EventBus.getDefault().postSticky(detailActivityData);
                                Intent intent = new Intent(getApplicationContext(), PropertyDetail.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                getApplicationContext().startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "No Properties Found", Toast.LENGTH_LONG).show();

                            }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILTER_REQUEST:
                extension=launchActivityInterface.checkResponse(response,zipcode);
                if (resultCode == FilterActivity.RESULT_OK) {
                    extension = filterUtility.applyFilterData(data,extension);
                    sendRequestAndprintResponse(extension);
                break;
            }
            case PICK_SORT_REQUEST:
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == SortActivity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "SORT OPTION SELECTED" + sortUtility.applySortData(data, extension), Toast.LENGTH_LONG).show();
                }
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

    @Override
    public void onCameraIdle() {
        currentLocation = googleMap.getCameraPosition().target;
        Log.d(TAG, "map has stopped moving, current location: " + currentLocation);
        extension = "/cordinate?longitude="+String.valueOf(currentLocation.longitude)+"&latitude="+String.valueOf(currentLocation.latitude);
        extension = "/zdata?zipcode=95126";
        Log.d(TAG,"inside  Camera Idle" +response);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response = userInput.getText().toString();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                //extension = "/zdata?zipcode="+response;
                //Log.d(TAG,"On clickinggggggggg   response is" +response);
            }
        });
        if(response != null){
            //propertyList.clear();
            googleMap.clear();
            extension = "/zdata?zipcode="+response;
        }
        //googleMap.clear();
        sendRequestAndprintResponse(extension);
    }
}

