package edu.sjsu.hivo.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.PropertyListAdapter;
import edu.sjsu.hivo.events.MainActivityData;
import edu.sjsu.hivo.model.Property;
import edu.sjsu.hivo.networking.VolleyNetwork;

public class MainActivity extends AppCompatActivity  {

    String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private PropertyListAdapter adapter;
    private List<Property> propertyList;
    private TextView mapTextView;
    private ImageView mapImageView;
    static final int MY_PERMISSIONS_REQUEST_INTERNET = 110;
    private static final int TAG_CODE_PERMISSION_LOCATION = 110;
    static final int PICK_FILTER_REQUEST = 1;  // The request code

    String onlyOpenHouse="", maxPrice="", minPrice="", maxSqft="", minSqft="", noOfBeds="", noOfBaths="";


    PlacesAutocompleteTextView userInput;
    String userText,extension;
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
                                    Log.d("TEST:", " Details: " + details.address_components.toString());
                                    extension="/cordinate?longitude="+String.valueOf(lon)+"&latitude="+String.valueOf(lat);
                                    sendRequestAndprintResponse(extension);
                    }
                                @Override
                                public void onFailure(final Throwable failure) {
                                    Log.d("test", "failure " + failure);
                                }
                            });
                }
        });
        userText = String.valueOf(userInput.getText());

        mapTextView = findViewById(R.id.list_map_tv);
        mapImageView = findViewById(R.id.list_map_iv);
        moveToMapVew();

        adapter = new PropertyListAdapter(propertyList,this);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);


        if (savedInstanceState != null) {
            MainActivityData data = EventBus.getDefault().getStickyEvent(MainActivityData.class);
            propertyList.addAll(data.getProperties());
            adapter.notifyDataSetChanged();
        } else {
            sendRequestAndprintResponse("/zdata?zipcode=95126");
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICK_FILTER_REQUEST: {
                extension="zdata?zipcode=94539";
                if (resultCode == FilterActivity.RESULT_OK) {
                    maxPrice= data.getStringExtra("MAX_PRICE");
                    if (!maxPrice.equals("") ){
                        extension += "&price=" + maxPrice + "&price_op=lt" +
                                "";
                        Log.d("TEST","maxPr "+maxPrice);
                    }

                    minPrice = data.getStringExtra("MIN_PRICE");
                    if (!minPrice.equals("")) {
                        Log.d("TEST","minPrice "+minPrice);
                        extension += "&price=" + minPrice + "&price_op=gt";
                    }
                    maxSqft = data.getStringExtra("MAX_SQFT");
                    if (!maxSqft.equals("")) {
                        extension += "&sqft=" + maxSqft + "&sqft_op=lt";
                        Log.d("TEST","maxSqft "+maxSqft);
                    }
                    minSqft = data.getStringExtra("MIN_SQFT");
                    if (!minSqft.equals("")) {
                        extension += "&sqft=" + minSqft + "&sqft_op=gt";
                        Log.d("TEST","minqsft "+minSqft);

                    }
                    noOfBeds = data.getStringExtra("NO_OF_BEDS");
                    if (!noOfBeds.equals("0")) {
                        Log.d("TEST","noOfBeds "+noOfBeds);
                        extension += "&beds=" + noOfBeds + "&beds_op=eq";
                    }
                    noOfBaths = data.getStringExtra("NO_OF_BATHS");
                    if (!noOfBaths.equals("0.0")) {
                        Log.d("TEST","noOfBaths "+noOfBaths);
                        extension += "&baths=" + noOfBaths + "&baths_op=eq";
                    }

                    Log.d("TEST","Extension"+extension);
                    sendRequestAndprintResponse(extension);
                    Log.i("**TAG*************",onlyOpenHouse+" "+maxPrice+" "+minPrice+" "+maxSqft+" "+ minSqft+" "+noOfBeds+" "+noOfBaths);
                }
                break;
            }
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

    public void sendRequestAndprintResponse(final String extension) {
        checkPermission();
        Log.d(TAG, "inside sendRequestAndprintResponse()" + VolleyNetwork.AWS_ENDPOINT + extension);
        try {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    VolleyNetwork.AWS_ENDPOINT + extension,
                    null,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "response is:" + response.toString());
                            Type listType = new TypeToken<ArrayList<Property>>(){}.getType();
                            List<Property> list = new Gson().fromJson(response.toString(), listType);
                            propertyList.clear();
                            propertyList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error == null || error.networkResponse == null) {
                                if (error != null) Log.e(TAG, "..", error);
                                return;
                            }

                            Log.e(TAG, "error making server request" + error.getMessage());
                            Toast.makeText(MainActivity.this, "error: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
        } catch (Exception e) {
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
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (propertyList != null) {
            EventBus.getDefault().postSticky(new MainActivityData(propertyList));
        }
    }


}
