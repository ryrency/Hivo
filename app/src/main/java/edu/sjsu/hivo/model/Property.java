package edu.sjsu.hivo.model;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/* Collecting the List of Response from JSON*/
public class Property {
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

    public Property(){

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
            saleType = getString("SALE TYPE");
            if(saleType != null && saleType.equals("PAST SALE")) saleType = "Sold";
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

        private Property(JSONObject data) {
            this.data = data;
        }
        public   JSONObject getData() {
        return data;
    }

        public  String getPrice(){
            price = getValueInStringFromDouble(getString("PRICE"));

            return "$"+price;

        }

        public  String getAddress(){
            return getString("ADDRESS");
        }

        public String getAddress2(){
            return getString("CITY")+", "+getValueInStringFromDouble(getString("ZIP"));
        }


        @Nullable
        public static Property fromJSONObjectResponse(JSONObject response) {
            return new Property(response);
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

        private String getValueInStringFromDouble(String value){

            value = !value.contains(".")? value :
                    value.replaceAll("0*$", "").
                            replaceAll("\\.$", "");
            return  value;

        }

    }


