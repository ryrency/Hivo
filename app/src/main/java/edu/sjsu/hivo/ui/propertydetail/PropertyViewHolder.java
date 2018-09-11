package edu.sjsu.hivo.ui.propertydetail ;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.model.Property;
import edu.sjsu.hivo.ui.propertydetail.*;

public abstract class PropertyViewHolder extends RecyclerView.ViewHolder{
    ViewPager viewPager;
    public PropertyViewHolder(View view) {
        super(view);
//        viewPager = (ViewPager)view.findViewById(R.id.detail_viewpager);

    }

    public abstract void bindProperty(Property property);
}
