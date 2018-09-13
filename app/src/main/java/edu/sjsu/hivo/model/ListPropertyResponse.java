package edu.sjsu.hivo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ListPropertyResponse {
    private JSONObject data;
    private String zip;
    private String propertyType = "Condo";
    private String state;
    private String saleType = "Open";
    private String city;
    private String url;
    private String interested;
    private String price = "$7,50000";
    private String address1 = "754,The Alameda";
    private String address2 = "SanJose, 95126";
    private String lotSize = "3,000";
    private String area = "1,086";
    private String pricePerSqFt = "$687";
    private String builtYear = "1962";
    private String beds = "3";
    private String baths = "2";
    private String id;
    private String favorite;
    private double longitude ;
    private double latitude ;

    public ListPropertyResponse(){

    }

        public String getPropertyType() {
            //return propertyType;
            propertyType = getString("PROPERTY TYPE");
            if (propertyType != null && propertyType.equals("Single Family Residential")){
                propertyType = "Single Family";
            }
            return this.propertyType;
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
        public   JSONObject getData() {
        return data;
    }

        public  String getPrice(){
            String price = '$'+ getString("PRICE");
            return price;

        }

        public  String getAddress(){
            return getString("ADDRESS");
        }

        public String getAddress2(){
            return getString("CITY")+", "+getString("ZIP");
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


