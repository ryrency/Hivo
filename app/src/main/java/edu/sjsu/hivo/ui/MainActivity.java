package edu.sjsu.hivo.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.PropertyListAdapter;

import edu.sjsu.hivo.model.ListPropertyResponse;
import edu.sjsu.hivo.networking.VolleyNetwork;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private PropertyListAdapter adapter;
    private ArrayList<Object> propertyList;
    static final int MY_PERMISSIONS_REQUEST_INTERNET = 110;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        ArrayList<Object> propertyList = new ArrayList<>();
//        propertyList.add(new Property());
//        propertyList.add(new Property());
//        propertyList.add(new Property());
//        propertyList.add(new Property());
//        propertyList.add(new Property());
        recyclerView = (RecyclerView)findViewById(R.id.property_details_rv);

        propertyList = new ArrayList<>();
        sendRequestAndprintResponse("94560");
//        recyclerView = (RecyclerView)findViewById(R.id.list_property_rv);

        adapter = new PropertyListAdapter(propertyList,this);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setAdapter(adapter);


    }

    private void addListObject(Object listObject) {
        propertyList.add(listObject);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(propertyList.size() - 1);
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
                            Toast.makeText(MainActivity.this, "response: "+response, Toast.LENGTH_LONG).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error == null || error.networkResponse == null) {
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
            return;
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


}
