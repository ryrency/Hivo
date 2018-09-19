package edu.sjsu.hivo.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    PlacesAutocompleteTextView userInput;
    String userText;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.property_details_rv);
        propertyList = new ArrayList<>();

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
        Log.d(TAG,"inside sendRequestAndprintResponse()"+VolleyNetwork.AWS_ENDPOINT+"/dummy?zipcode="+zipcode);
        try{
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    VolleyNetwork.AWS_ENDPOINT+"/dummy?zipcode="+zipcode,
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


//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        Toast.makeText(this,"before changes",Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        Toast.makeText(this,"during changes",Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        Toast.makeText(this,"after changes",Toast.LENGTH_LONG).show();
//        try{
//            userText = s.toString();
//            double[] list = new GeoCoderAccessor(MainActivity.this).getList(userText);
//            if (list != null)
//                Log.i(TAG, "latitude sent by geocoder is: "+list[0] + " and longitude is:  " + list[1]);
//            sendRequestAndprintResponse(userText);
//
//        }
//        catch (NumberFormatException e){
//            e.printStackTrace();
//        }
//    }
}
