package edu.sjsu.hivo.ui.propertydetail;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

    Parcelable state;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_detail_images);
        viewPager = (ViewPager)findViewById(R.id.detail_viewpager_only);
        houseImages = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        houseImages = extras.getIntegerArrayList("LIST");
        position = (int) extras.get("POSITION");

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("currentItem", 0);
        }
        adapter = new CustomPagerAdapter(this,houseImages,position);


        viewPager.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        position = viewPager.getCurrentItem();
        outState.putInt("currentItem", position);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = viewPager.onSaveInstanceState();
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
