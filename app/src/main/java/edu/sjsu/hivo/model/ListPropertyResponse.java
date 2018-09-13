package edu.sjsu.hivo.model;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class ListPropertyResponse {
    private JSONObject data;
    private String zip;
    private final String propertyType = "Condo";
    private String state;
    private final String saleType = "Open";
    private String city;
    private String url;
    private String interested;
    private final String price = "$7,50000";
    private final String address1 = "754,The Alameda";
    private final String address2 = "SanJose, 95126";
    private final String lotSize = "3,000";
    private final String area = "1,086";
    private final String pricePerSqFt = "$687";
    private final String builtYear = "1962";
    private final String beds = "3";
    private final String baths = "2";
    private String id;
    private String favorite;
    private final double longitude = -121.9073828;
    private final double latitude = 37.3315984;

    public ListPropertyResponse(){

    }

        public String getPropertyType() {
            return propertyType;
        }

        public String getSaleType() {
            return saleType;
        }

        public String getLotSize() {
            return lotSize;
        }

        public String getArea() {
            return area;
        }

        public String getPricePerSqFt() {
            return pricePerSqFt;
        }

        public String getBuiltYear() {
            return builtYear;
        }

        public String getBeds() {
            return beds;
        }

        public String getBaths() {
            return baths;
        }


        public double getLatitude(){
        return Double.valueOf(getString("LATITUDE"));
    }

        public double getLongitude(){
        //return longitude;
            return Double.valueOf(getString("LONGITUDE"));
        }
        private ListPropertyResponse(JSONObject data) {
            this.data = data;
        }
        private  JSONObject getData() {
        return data;
    }

    public  String getPrice(){
        String price  = getString("PRICE");
        return price;
    }
    public  String getAddress(){
            //return address1;
            return getString("ADDRESS");
    }
    public String getAddress2(){
            //return address2;
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
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }




    }


