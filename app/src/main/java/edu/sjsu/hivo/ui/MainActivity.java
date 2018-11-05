package edu.sjsu.hivo.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    private JSONObject jsonObject;
    private ImageView filterImg,sortImg;
    private TextView filterText,sortText;
    private Context context;
    private FilterUtility filterUtility;
    private SortUtility sortUtility;

    static final int PICK_FILTER_REQUEST = 1;  // The request code
    static final int PICK_SORT_REQUEST = 2;  // The request code
    static final int MY_PERMISSIONS_REQUEST_INTERNET = 110;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        propertyList = new ArrayList<>();

        getXmlReferences();
        checkPermission();

        userInput.clearFocus();
        filterUtility = new FilterUtility(this);
        filterUtility.getFilterData(filterImg,filterText);

        sortUtility = new SortUtility(this);
        sortUtility.getSortData(sortImg,sortText);

        setAutoPlaceComplete();
        moveToMapVew();

        adapter = new PropertyListAdapter(propertyList,this);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);

        setEnterButtonListener();
        checkSavedInstance(savedInstanceState);

    }

    private void getXmlReferences(){
        recyclerView = findViewById(R.id.property_details_rv);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        filterImg = (ImageView)findViewById(R.id.list_filter_iv);
        filterText = (TextView)findViewById(R.id.list_filter_tv);
        sortImg = (ImageView)findViewById(R.id.list_sort_iv);
        sortText = (TextView)findViewById(R.id.list_sort_tv);
        enterButton = (ImageView) findViewById(R.id.enter_button);
        userInput = (PlacesAutocompleteTextView) findViewById(R.id.enter_location);
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
                    public void onPlaceSelected(final Place place) {
                        userInput.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(final PlaceDetails details) {
                                Log.d("test", "details " + details);
                                Double lat = details.geometry.location.lat;
                                Double lon = details.geometry.location.lng;
                                Log.d("TEST:", " Details: " + details.address_components.toString());
                                String address = details.address_components.get(0).short_name+" "+ /*street No*/
                                        details.address_components.get(1).short_name;/*Adddress Line 1*/

                                try {
                                    address = URLEncoder.encode(address, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                extension = "/data?str="+address;
                                sendRequestAndprintResponse(extension);
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

                extension=launchActivityInterface.checkResponse(response);
                sendRequestAndprintResponse(extension);

            }
        });
    }

    private void checkSavedInstance(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            MainActivityData data = EventBus.getDefault().getStickyEvent(MainActivityData.class);
            if (data.getProperties() != null) {
                propertyList.addAll(data.getProperties());
                adapter.notifyDataSetChanged();
            } else {
                sendRequestAndprintResponse("/zdata?zipcode=95126");
            }
        } else {
            sendRequestAndprintResponse("/zdata?zipcode=95126");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILTER_REQUEST:
                extension = launchActivityInterface.checkResponse(response);
                if (resultCode == FilterActivity.RESULT_OK) {
                    extension = filterUtility.applyFilterData(data, extension);
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
    public void sendRequestAndprintResponse(final String extension) {
        checkPermission();
        hideKeyboard(this);
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

                            if (propertyList.size() > 1) {
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(0);
                            }
                            else if (propertyList.size() ==  1){
                                Log.d("RENCY", "Into size==1");
                                DetailActivityData detailActivityData = new DetailActivityData(propertyList.get(0));
                                EventBus.getDefault().postSticky(detailActivityData);
                                Intent intent = new Intent(context, PropertyDetail.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                context.startActivity(intent);
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
