package edu.sjsu.hivo.ui.propertydetail ;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import edu.sjsu.hivo.R;
import edu.sjsu.hivo.adapter.CustomPagerAdapter;
import edu.sjsu.hivo.model.Property;

public class ViewPagerViewHolder extends PropertyViewHolder {
    ViewPager viewPager;
    Context context;
    CustomPagerAdapter adapter;
    View view;
    public ViewPagerViewHolder(Context context, View view) {
        super(view);
        this.view = view;
        this.context = context;
    }

    @Override
    public void bindProperty(Property property) {
        ArrayList<Integer> houseImages = new ArrayList<>();
        viewPager = (ViewPager)view.findViewById(R.id.detail_viewpager);
        houseImages.add(R.drawable.house1);
        houseImages.add(R.drawable.house2);
        houseImages.add(R.drawable.house3);
        houseImages.add(R.drawable.house4);
        houseImages.add(R.drawable.house5);
        houseImages.add(R.drawable.house6);
        adapter = new CustomPagerAdapter(context,houseImages);

        viewPager.setAdapter(adapter);
    }
}
