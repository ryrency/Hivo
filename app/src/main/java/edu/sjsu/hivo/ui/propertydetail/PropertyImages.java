package edu.sjsu.hivo.ui.propertydetail;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.CustomPagerAdapter;

public class PropertyImages extends AppCompatActivity{
    ViewPager viewPager;
    CustomPagerAdapter adapter;
    ArrayList<Integer> houseImages;
    int position;
    String TAG =  PropertyImages.class.getSimpleName();
    boolean orientationLand;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"Into Create....************");
        setContentView(R.layout.property_detail_images);
        viewPager = (ViewPager)findViewById(R.id.detail_viewpager_only);
        houseImages = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        houseImages = extras.getIntegerArrayList("LIST");
        position = (int) extras.get("POSITION");

        adapter = new CustomPagerAdapter(this,houseImages,position);

        viewPager.setAdapter(adapter);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of item position
        outState.putInt("SELECTED_ITEM_POSITION", adapter.getItemPosition(houseImages));
        outState.putIntegerArrayList("LIST", houseImages);
        Log.i(TAG, "into OnSavedStateInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Read the state of item position
        int mPosition = savedInstanceState.getInt("SELECTED_ITEM_POSITION");
        ArrayList<Integer> list = savedInstanceState.getIntegerArrayList("LIST");
        Log.i(TAG, "into onRestoreInstanceState");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //Update the Flag here
        orientationLand = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? true : false);
        Log.i(TAG,"Orientaion Landscape? "+orientationLand);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Into OnPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Into OnRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Into onResume");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Into onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Into onDestroy");

    }

}