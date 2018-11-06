package edu.sjsu.hivo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.events.DetailActivityData;
import edu.sjsu.hivo.model.Property;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetail;
//import edu.sjsu.hivo.model.PropertyList;
//import edu.sjsu.hivo.ui.MainActivity;
//import edu.sjsu.hivo.ui.PropertyDetail;


public class PropertyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Property> propertyList;
    private final Context context;
    private String TAG = PropertyListAdapter.class.getSimpleName();

    public PropertyListAdapter(List<Property> propertyList, Context context){
        this.propertyList = propertyList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        Log.i(TAG,"INTO ADATPER"+propertyList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Log.i(TAG,"INTO create viewholder "+propertyList.size());
        View view  = layoutInflater.inflate(R.layout.property_listing_item, parent,false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh1 = (MyViewHolder)holder;
        Log.i(TAG,"INTO create bindholder "+propertyList.size());
        Property response = propertyList.get(position);
        vh1.bindData(response);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG,"INTO getItemCount "+propertyList.size());
        return propertyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView propertyIv;
        private final  TextView propertyPriceTv;
        private TextView propertyAddressLine1Tv;
        private TextView propertyAddressLine2Tv;
        private TextView propertyBedNoTv;
        private TextView propertyBathNoTv;
        private TextView propertySqftNoTv;


        MyViewHolder(View itemView){
            super(itemView);
            propertyIv = itemView.findViewById(R.id.property_iv);
            propertyPriceTv = itemView.findViewById(R.id.property_price_tv);
            propertyAddressLine1Tv = itemView.findViewById(R.id.property_address_line1_tv);
            propertyAddressLine2Tv =  itemView.findViewById(R.id.property_address_line2_tv);
            propertyBedNoTv = itemView.findViewById(R.id.property_bed_no_tv);
            propertyBathNoTv = itemView.findViewById(R.id.property_baths_no_tv);
            propertySqftNoTv = itemView.findViewById(R.id.property_sqft_no_tv);

        }

        void bindData(final Property property) {
            Log.i(TAG,"in bindData "+propertyList.size());
            Glide.with(context).load(property.getUrls().get(1)).into(propertyIv);
            propertyPriceTv.setText(property.getPrice());
            propertyAddressLine1Tv.setText(property.getAddress());
            propertyAddressLine2Tv.setText(property.getAddress2());
            propertyBedNoTv.setText(property.getBeds());
            propertyBathNoTv.setText(property.getBaths());
            propertySqftNoTv.setText(property.getArea());

            propertyIv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    DetailActivityData detailActivityData = new DetailActivityData(property);
                    EventBus.getDefault().postSticky(detailActivityData);
                    Intent intent = new Intent(context, PropertyDetail.class);
                    context.startActivity(intent);
                }
            });


        }


    }

}
