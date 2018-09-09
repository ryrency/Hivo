package edu.sjsu.hivo.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.CustomPagerAdapter;

public class PropertyDetail extends AppCompatActivity {
    String TAG = PropertyDetail.class.getSimpleName();
    ViewPager viewPager;
    CustomPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_images);
        Log.i(TAG,"intoProperty DEtail");
        ArrayList<Integer> houseImages = new ArrayList<>();

        houseImages.add(R.drawable.house1);
        houseImages.add(R.drawable.house2);
        houseImages.add(R.drawable.house3);
        houseImages.add(R.drawable.house4);
        houseImages.add(R.drawable.house5);
        houseImages.add(R.drawable.house6);

        viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
//        tabLayout.setupWithViewPager(viewPager, true);

        adapter = new CustomPagerAdapter(this,houseImages);

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
