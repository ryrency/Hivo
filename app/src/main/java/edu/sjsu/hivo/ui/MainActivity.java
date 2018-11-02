package edu.sjsu.hivo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.PropertyListAdapter;

import edu.sjsu.hivo.model.Property;
import edu.sjsu.hivo.networking.VolleyNetwork;

public class MainActivity extends AppCompatActivity  {

    String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private PropertyListAdapter adapter;
    private ArrayList<Object> propertyList;
    private TextView mapTextView;
    private ImageView mapImageView;
    static final int MY_PERMISSIONS_REQUEST_INTERNET = 110;
    private static final int TAG_CODE_PERMISSION_LOCATION = 110;
    static final int PICK_FILTER_REQUEST = 1;  // The request code

    String onlyOpenHouse="", maxPrice="", minPrice="", maxSqft="", minSqft="", noOfBeds="", noOfBaths="";

    PlacesAutocompleteTextView userInput;
    String userText;
    JSONObject jsonObject;
    ImageView filterImg;
    TextView filterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.property_details_rv);
        propertyList = new ArrayList<>();

        filterImg = (ImageView)findViewById(R.id.list_filter_iv);
        filterText = (TextView)findViewById(R.id.list_filter_tv);

        getFilterData();

        userInput = (PlacesAutocompleteTextView) findViewById(R.id.enter_location);
        sendRequestAndprintResponse("95126");
        userInput.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                            userInput.getDetailsFor(place, new DetailsCallback() {
                                @Override
                                public void onSuccess(final PlaceDetails details) {
                                    Log.d("test", "details " + details);
                                    Double lat = details.geometry.location.lat;
                                    Double lon = details.geometry.location.lng;
                                    Log.d("test", "lat and long  is ... " + lat+" "+lon);
                    }
                                @Override
                                public void onFailure(final Throwable failure) {
                                    Log.d("test", "failure " + failure);
                                }
                            });
                }
        });


       // userInput.addTextChangedListener(this);

        //sendRequestAndprintResponse("95126");

        mapTextView = findViewById(R.id.list_map_tv);
        mapImageView = findViewById(R.id.list_map_iv);
        moveToMapVew();

        adapter = new PropertyListAdapter(propertyList,this);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void addListObject(Object listObject) {
        propertyList.add(listObject);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFilterData(){
        filterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent filterIntent = new Intent(context , FilterActivity.class);
                startActivityForResult(filterIntent,PICK_FILTER_REQUEST);
//                context.startActivity(filterIntent);
            }
        });

        filterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent filterIntent = new Intent(context , FilterActivity.class);
                startActivityForResult(filterIntent,PICK_FILTER_REQUEST);

//                context.startActivity(filterIntent);
            }
        });
    }

    private void moveToMapVew() {

        mapImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, MapActivity.class);
                context.startActivity(intent);
            }
        });
            mapTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MapActivity.class);
                    context.startActivity(intent);

                }
            });


    }

    public void sendRequestAndprintResponse(final String zipcode) {
        checkPermission();
        Log.d(TAG,"inside sendRequestAndprintResponse()"+VolleyNetwork.AWS_ENDPOINT+"cordinate?longitude=-117.024779&latitude=32.837635");
        try{
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    VolleyNetwork.AWS_ENDPOINT+"/cordinate?longitude=-122.0305563&latitude=37.3224014",
//                    VolleyNetwork.AWS_ENDPOINT+"/95014",
                    null,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response){
                            Log.d(TAG,"response is:" + response.toString());
                            Property property = null;
                            try {
                                for (int i = 0; i < response.length(); ++i) {
                                    JSONObject rec = response.getJSONObject(i);
                                    property = Property.fromJSONObjectResponse(rec);
                                    addListObject(property);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error == null || error.networkResponse == null) {
                                if (error != null) Log.e(TAG, "..", error);
                                return;
                            }

                            Log.e(TAG, "error making server request"+error.getMessage());
                            Toast.makeText(MainActivity.this, "error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(Request);

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "NO PERMISSION GRANTED", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICK_FILTER_REQUEST: {
                if (resultCode == FilterActivity.RESULT_OK) {
                    maxPrice= data.getStringExtra("MAX_PRICE");
                    minPrice = data.getStringExtra("MIN_PRICE");
                    maxSqft = data.getStringExtra("MAX_SQFT");
                    minSqft = data.getStringExtra("MIN_SQFT");
                    noOfBeds = data.getStringExtra("NO_OF_BEDS");
                    noOfBaths = data.getStringExtra("NO_OF_BATHS");
                    onlyOpenHouse = data.getStringExtra("ONLY_OPEN_HOUSE");
                    Log.i("**TAG*************",onlyOpenHouse+" "+maxPrice+" "+minPrice+" "+maxSqft+" "+ minSqft+" "+noOfBeds+" "+noOfBaths);
                }
                break;
            }
        }
    }
}
