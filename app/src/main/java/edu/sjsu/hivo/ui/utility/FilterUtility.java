package edu.sjsu.hivo.ui.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.hivo.ui.FilterActivity;


public class FilterUtility {

    private static final int PICK_FILTER_REQUEST = 1;  // The request code

    private Activity mainActivity;

    public FilterUtility(Activity ac){
        mainActivity = ac;
    }

    public void getFilterData(ImageView filterImg,
            TextView filterText){
        filterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent filterIntent = new Intent(context , FilterActivity.class);
                mainActivity.startActivityForResult(filterIntent,PICK_FILTER_REQUEST);
            }
        });

        filterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent filterIntent = new Intent(context , FilterActivity.class);
                mainActivity.startActivityForResult(filterIntent,PICK_FILTER_REQUEST);
            }
        });
    }


    public String applyFilterData(Intent data, String extension){
        String  maxPrice, minPrice, maxSqft, minSqft, noOfBeds, noOfBaths;
        maxPrice= data.getStringExtra("MAX_PRICE");
            if (!maxPrice.equals("") ){
                extension += "&price=" + maxPrice + "&price_op=lt" +
                        "";
                Log.d("TEST","maxPr "+maxPrice);
            }

            minPrice = data.getStringExtra("MIN_PRICE");
            if (!minPrice.equals("")) {
                Log.d("TEST","minPrice "+minPrice);
                extension += "&price=" + minPrice + "&price_op=gt";
            }
            maxSqft = data.getStringExtra("MAX_SQFT");
            if (!maxSqft.equals("")) {
                extension += "&sqft=" + maxSqft + "&sqft_op=lt";
                Log.d("TEST","maxSqft "+maxSqft);
            }
            minSqft = data.getStringExtra("MIN_SQFT");
            if (!minSqft.equals("")) {
                extension += "&sqft=" + minSqft + "&sqft_op=gt";
                Log.d("TEST","minqsft "+minSqft);

            }
            noOfBeds = data.getStringExtra("NO_OF_BEDS");
            if (!noOfBeds.equals("0")) {
                Log.d("TEST","noOfBeds "+noOfBeds);
                extension += "&beds=" + noOfBeds + "&beds_op=eq";
            }
            noOfBaths = data.getStringExtra("NO_OF_BATHS");
            if (!noOfBaths.equals("0.0")) {
                Log.d("TEST","noOfBaths "+noOfBaths);
                extension += "&baths=" + noOfBaths + "&baths_op=eq";
            }

            Log.d("TEST","Extension"+extension);
            Log.i("**TAG*************",maxPrice+" "+minPrice+" "+maxSqft+" "+ minSqft+" "+noOfBeds+" "+noOfBaths);
            return extension;

    }

}
