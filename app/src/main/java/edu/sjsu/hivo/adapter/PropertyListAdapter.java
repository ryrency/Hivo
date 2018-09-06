package edu.sjsu.hivo.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.model.PropertyList;

public class PropertyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final ArrayList<Object> propertyList;
    private final Context context;
    String TAG = PropertyListAdapter.class.getSimpleName();

    public PropertyListAdapter(ArrayList<Object> propertyList, Context context){
        this.propertyList = propertyList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        Log.i(TAG,"INTO ADATPER");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        View view  = layoutInflater.inflate(R.layout.property_listing_item, parent,false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh1 = (MyViewHolder)holder;
        PropertyList list = (PropertyList)propertyList.get(position);
        ((MyViewHolder) holder).property_iv.setBackgroundResource(R.drawable.house1);
        ((MyViewHolder) holder).property_price_tv.setText(list.getProperty_price_tv());
        ((MyViewHolder) holder).property_address_line1_tv.setText(list.getProperty_address_line1_tv());
        ((MyViewHolder) holder).property_address_line2_tv.setText(list.getProperty_address_line2_tv());
        ((MyViewHolder) holder).property_bed_no_tv.setText(list.getProperty_bed_no_tv());
        ((MyViewHolder) holder).property_bath_no_tv.setText(list.getProperty_bath_no_tv());
        ((MyViewHolder) holder).property_sqft_no_tv.setText(list.getProperty_sqft_no_tv());
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
//        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView property_iv;
        public TextView property_price_tv;
        public TextView property_address_line1_tv;
        public TextView property_address_line2_tv;
        public TextView property_bed_no_tv;
        public TextView property_bath_no_tv;
        public TextView property_sqft_no_tv;

        public MyViewHolder(View itemView){
            super(itemView);
            property_iv = (ImageView)itemView.findViewById(R.id.property_iv);
            property_price_tv = (TextView)itemView.findViewById(R.id.property_price_tv);
            property_address_line1_tv= (TextView)itemView.findViewById(R.id.property_address_line1_tv);
            property_address_line2_tv = (TextView) itemView.findViewById(R.id.property_address_line2_tv);
            property_bed_no_tv = (TextView)itemView.findViewById(R.id.property_bed_no_tv);
            property_bath_no_tv = (TextView)itemView.findViewById(R.id.property_bath_no_tv);
            property_sqft_no_tv = (TextView)itemView.findViewById(R.id.property_sqft_no_tv);
        }


    }

}
