package edu.sjsu.hivo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ListPropertyResponse implements Parcelable{
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
    private double longitude = -121.9073828;
    private double latitude = 37.3315984;

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
        return latitude;
    }

        public double getLongitude(){
        return longitude;
        }


        private ListPropertyResponse(JSONObject data) {
            this.data = data;
        }
        public   JSONObject getData() {
        return data;
    }

    public  String getPrice(){
//        return price;
        return getString("PRICE");

    }
    public String getParcelPrice(){
        return this.price;
    }

    public  String getAddress(){
//            return address1;
         return getString("ADDRESS");
    }

    public String getParcelAddress(){
        return this.address1;
    }

    public String getAddress2(){
//            return address2;
            return getString("CITY")+getString("STATE")+getString("ZIP");
    }

    public String getParcelAddress2(){
        return this.address2;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(propertyType);
        dest.writeString(state);
        dest.writeString(saleType);
        dest.writeString(city);
        dest.writeString(url);
        dest.writeString(interested);
        dest.writeString(price);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(lotSize);
        dest.writeString(area);
        dest.writeString(pricePerSqFt);
        dest.writeString(builtYear);
        dest.writeString(beds);
        dest.writeString(baths);
        dest.writeString(id);
        dest.writeString(favorite);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    private ListPropertyResponse(Parcel in){
        this.propertyType = in.readString();
        this.state = in.readString();
        this.saleType = in.readString();
        this.city = in.readString();
        this.url = in.readString();
        this.interested = in.readString();
        this.price = in.readString();
        this.address1 = in.readString();
        this.address2 = in.readString();
        this.lotSize = in.readString();
        this.area = in.readString();
        this.pricePerSqFt = in.readString();
        this.builtYear = in.readString();
        this.beds = in.readString();
        this.baths = in.readString();
        this.id = in.readString();
        this.favorite = in.readString();
        this.longitude = in.readDouble();
        this.latitude=in.readDouble();

    }

    public static final Parcelable.Creator<ListPropertyResponse> CREATOR = new Parcelable.Creator<ListPropertyResponse>() {
        @Override
        public ListPropertyResponse createFromParcel(Parcel source) {
            return new ListPropertyResponse(source);
        }

        @Override
        public ListPropertyResponse[] newArray(int size) {
            return new ListPropertyResponse[size];
        }
    };
    }


