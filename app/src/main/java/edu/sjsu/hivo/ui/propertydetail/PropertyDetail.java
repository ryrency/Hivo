package edu.sjsu.hivo.ui.propertydetail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import edu.sjsu.hivo.events.DetailActivityData;
import edu.sjsu.hivo.events.MainActivityData;
import edu.sjsu.hivo.model.Property;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.CustomPagerAdapter;
;

public class PropertyDetail extends AppCompatActivity {
    private static final String TAG = PropertyDetail.class.getSimpleName();
    private RecyclerView recyclerView;
    private PropertyDetailAdapter propertyDetailAdapter;
    ViewPager viewPager;
    CustomPagerAdapter adapter;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_details);
        Log.i(TAG,"intoProperty DEtail");

        Property property = ((DetailActivityData)EventBus.getDefault().getStickyEvent(DetailActivityData.class)).getProperty();
        Log.i(TAG,"getting property object"+ property.getPrice());

        recyclerView = (RecyclerView)findViewById(R.id.property_details_rv);
        propertyDetailAdapter = new PropertyDetailAdapter(this, property);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(PropertyDetail.this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(propertyDetailAdapter);
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
