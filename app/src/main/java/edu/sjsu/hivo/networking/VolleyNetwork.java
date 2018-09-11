package edu.sjsu.hivo.networking;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyNetwork {
    public static final String AWS_ENDPOINT = "https://whispering-headland-86787.herokuapp.com";
            //"http://192.168.0.2:8080";

    private static VolleyNetwork mInstance;
    private RequestQueue mrequestQueue;
    //private Context context;
    public static VolleyNetwork getInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleyNetwork(context);
        }
        return mInstance;
    }

    private VolleyNetwork(Context context){
        mrequestQueue = getRequestQueue(context);

    }

    public RequestQueue getRequestQueue(Context context){
        if(mrequestQueue == null){
            mrequestQueue = Volley.newRequestQueue(context);
        }

        return mrequestQueue;
    }


}

