package edu.sjsu.hivo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.PropertyListAdapter;
import edu.sjsu.hivo.events.DetailActivityData;
import edu.sjsu.hivo.events.MainActivityData;
import edu.sjsu.hivo.model.Property;
import edu.sjsu.hivo.networking.VolleyNetwork;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetail;
import edu.sjsu.hivo.ui.utility.FilterUtility;
import edu.sjsu.hivo.ui.utility.SortUtility;

public class MainActivity extends AppCompatActivity  {
    String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private PropertyListAdapter adapter;
    private List<Property> propertyList;
    private TextView mapTextView;
    private ImageView mapImageView, enterButton;
    private static final int TAG_CODE_PERMISSION_LOCATION = 110;
    private String response="";
    private PlacesAutocompleteTextView userInput;
    private LaunchActivityInterface launchActivityInterface;
    private String userText,extension;
    private ImageView filterImg,sortImg;
    private TextView filterText,sortText;
    private Context context;
    private FilterUtility filterUtility;
    private SortUtility sortUtility;
    private String zipcode="";
    static final int PICK_FILTER_REQUEST = 1;  // The request code
    static final int PICK_SORT_REQUEST = 2;  // The request code
    static final int MY_PERMISSIONS_REQUEST_INTERNET = 110;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        propertyList = new ArrayList<>();

        getXmlReferences();
        checkPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            zipcode = launchActivityInterface.getZipcodeFromLocation(location,getApplicationContext());
                            extension = "/zdata?zipcode="+zipcode;
                            sendRequestAndprintResponse(extension,0);
                        }
                    }
                });

        userInput.clearFocus();
        filterUtility = new FilterUtility(this);
        filterUtility.setFilterListener(filterImg,filterText);

        sortUtility = new SortUtility(this);
        sortUtility.setSortListener(sortImg,sortText);

        setAutoPlaceComplete();
        moveToMapVew();

//        final Bundle savedState = savedInstanceState;


        adapter = new PropertyListAdapter(propertyList,this);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);

        checkSavedInstance(savedInstanceState);


        setEnterButtonListener();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "end of list reached in recycler view, may be load more from server.");
                    sendRequestAndprintResponse(extension,propertyList.size());
                }
            }
        });


    }

    private void getXmlReferences(){
        recyclerView = findViewById(R.id.property_details_rv);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        filterImg = findViewById(R.id.list_filter_iv);
        filterText = findViewById(R.id.list_filter_tv);
        sortImg = findViewById(R.id.list_sort_iv);
        sortText = findViewById(R.id.list_sort_tv);
        enterButton =  findViewById(R.id.enter_button);
        userInput =  findViewById(R.id.enter_location);
        userText = String.valueOf(userInput.getText());
        mapTextView = findViewById(R.id.list_map_tv);
        mapImageView = findViewById(R.id.list_map_iv);
        launchActivityInterface = new LaunchActivityImpl();
        context = getApplicationContext();
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

    private void setAutoPlaceComplete(){
        userInput.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(@NonNull final Place place) {
                        userInput.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(final PlaceDetails details) {
                                Log.d("test", "details " + details);
                                Log.d("TEST:", " Details: " + details.address_components.toString());
                                String address = details.address_components.get(0).short_name+" "+ /*street No*/
                                        details.address_components.get(1).short_name;/*Adddress Line 1*/

                                try {
                                    address = URLEncoder.encode(address, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                extension = "/data?str="+address;
                                sendRequestAndprintResponse(extension,0);
                                userInput.clearFocus();
                            }

                            @Override
                            public void onFailure(final Throwable failure) {
                                Log.d("test", "failure " + failure);
                            }
                        });
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


    private void setEnterButtonListener(){
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response = userInput.getText().toString();
                userInput.getText().clear();
                userInput.clearFocus();

                extension = launchActivityInterface.checkResponse(response, zipcode);
                sendRequestAndprintResponse(extension,0);

            }
        });
    }

    private void checkSavedInstance(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            MainActivityData data = EventBus.getDefault().getStickyEvent(MainActivityData.class);
            if (data.getProperties() != null) {
                propertyList.addAll(data.getProperties());
                adapter.notifyDataSetChanged();
            }
            else {
                extension="/zdata?zipcode="+zipcode;
                sendRequestAndprintResponse(extension,0);
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILTER_REQUEST:
                Log.d(TAG,"zipcode = "+zipcode);
                if (resultCode == FilterActivity.RESULT_OK) {
                    extension = launchActivityInterface.checkResponse(response,zipcode);
                    extension = filterUtility.applyFilterData(data, extension);//check if can be done better
                    sendRequestAndprintResponse(extension,0);
                    break;

                }
            case PICK_SORT_REQUEST:
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == SortActivity.RESULT_OK) {
                    extension = launchActivityInterface.checkResponse(response,zipcode);
                    extension +=  sortUtility.applySortData(data);
                    sendRequestAndprintResponse(extension,0);
                }
        }

    }
    public void sendRequestAndprintResponse(final String extension, final int skipCount) {
        checkPermission();
        hideKeyboard(this);
        Log.d(TAG, "inside sendRequestAndprintResponse()" + VolleyNetwork.AWS_ENDPOINT + extension);
        try {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    VolleyNetwork.AWS_ENDPOINT + extension+"&skip="+skipCount,
                    null,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "response is:" + response.toString());
                            Type listType = new TypeToken<ArrayList<Property>>() {
                            }.getType();
                            List<Property> list = new Gson().fromJson(response.toString(), listType);

                            if (list.size() > 1 && skipCount==0) {
                                propertyList.clear();
                                propertyList.addAll(list);
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(0);
                            } else if (propertyList.size() > 14) {
                                propertyList.addAll(list);
                                adapter.notifyDataSetChanged();
                            } else if (list.size() == 1) {
                                propertyList.clear();
                                propertyList.addAll(list);
                                DetailActivityData detailActivityData = new DetailActivityData(propertyList.get(0));
                                EventBus.getDefault().postSticky(detailActivityData);
                                Intent intent = new Intent(context, PropertyDetail.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                context.startActivity(intent);
                            }

                            if (propertyList.size() == 0)
                                Toast.makeText(getApplicationContext(), "No Properties Found " + propertyList.size(), Toast.LENGTH_LONG).show();
                            else if (list.size() == 0)
                                Toast.makeText(getApplicationContext(), "No More Properties Found " + propertyList.size(), Toast.LENGTH_LONG).show();
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
