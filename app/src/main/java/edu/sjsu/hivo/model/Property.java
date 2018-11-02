package edu.sjsu.hivo.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/* Property model, deserialized by Gson. */
public class Property {

    @SerializedName("ZIP")
    private String zip;

    @SerializedName("PROPERTY TYPE")
    private String propertyType;

    @SerializedName("STATE")
    private String state;

    @SerializedName("SALE TYPE")
    private String saleType;

    @SerializedName("CITY")
    private String city;

    @SerializedName("INTERESTED")
    private String interested;

    @SerializedName("PRICE")
    private String price;

    @SerializedName("ADDRESS")
    private String address;

    @SerializedName("LOT SIZE")
    private String lotSize = "3,000";

    @SerializedName("SQUARE FEET")
    private String area = "1,086";

    @SerializedName("BUILT")
    private String built = "1962";

    @SerializedName("BEDS")
    private String beds = "3";

    @SerializedName("BATHS")
    private String baths = "2";

    private String id;

    @SerializedName("FAVORITE")
    private String favorite;

    @SerializedName("BOUNDARY")
    private Boundary boundary;

    public Property() {

    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getPrice() {
        if (!TextUtils.isEmpty(price)) {
            return "$" + price;
        }
        return "Not known";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return city + ", " + state + " " + zip;
    }

    public String getLotSize() {
        if (!TextUtils.isEmpty(lotSize)) {
            return lotSize;
        }
        return "Not Known";
    }

    public void setLotSize(String lotSize) {
        this.lotSize = lotSize;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuilt() {
        return built;
    }

    public void setBuilt(String built) {
        this.built = built;
    }

    public String getBeds() {
        return beds;
    }

    public void setBeds(String beds) {
        this.beds = beds;
    }

    public String getBaths() {
        return baths;
    }

    public void setBaths(String baths) {
        this.baths = baths;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public boolean isLocationAvailable() {
        return boundary != null &&
                boundary.getCoordinates() != null &&
                boundary.getCoordinates().size() == 2;
    }

    public double getLatitude() {
        if (isLocationAvailable()) {
            return boundary.getCoordinates().get(1);
        }
        return 0.0;
    }

    public double getLongitude() {
        if (isLocationAvailable()) {
            return boundary.getCoordinates().get(0);
        }
        return 0.0;
    }

    private static class Boundary {
        private List<Double> coordinates;

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }
}


