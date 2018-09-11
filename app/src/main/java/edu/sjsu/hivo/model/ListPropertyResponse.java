package edu.sjsu.hivo.model;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

    public class ListPropertyResponse {
    private JSONObject data;
    private String zip;
    private String propertyType;
    private String price;
    private String longitude;
    private String state;
    private String saleType;
    private String city;
    private String url;
    private String interested;
    private String address;
    private String id;
    private String favorite;
    private String latitude;

        private ListPropertyResponse(JSONObject data) {
            this.data = data;
        }


        private  JSONObject getData() {
        return data;
    }

    public  String getPrice(){
        return getString("PRICE");
    }
    public  String getAddress(){
            return getString("ADDRESS");
    }
    public String getAddress2(){
            return getString("CITY")+getString("STATE")+getString("ZIP");
    }
    @Nullable
    public static ListPropertyResponse fromJSONObjectResponse(JSONObject response) {
        return new ListPropertyResponse(response);
    }

    @Nullable
    public  String getString(String key) {
        JSONObject data = getData();
        try {
            return data.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    }


