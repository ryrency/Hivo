package edu.sjsu.hivo.adapter;

import android.content.Context;
import android.content.Intent;
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
import edu.sjsu.hivo.model.Property;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetail;

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
        Property list = (Property)propertyList.get(position);
        vh1.bindData(list);
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
//        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView propertyIv;
        private final  TextView propertyPriceTv;
        private TextView propertyAddressLine1Tv;
        private TextView propertyAddressLine2Tv;
        private TextView propertyBedNoTv;
        private TextView propertyBathNoTv;
        private TextView propertySqftNoTv;

        public MyViewHolder(View itemView){
            super(itemView);
            propertyIv = (ImageView)itemView.findViewById(R.id.property_iv);
            propertyPriceTv = (TextView)itemView.findViewById(R.id.property_price_tv);
            propertyAddressLine1Tv = (TextView)itemView.findViewById(R.id.property_address_line1_tv);
            propertyAddressLine2Tv = (TextView) itemView.findViewById(R.id.property_address_line2_tv);
            propertyBedNoTv = (TextView)itemView.findViewById(R.id.property_bed_no_tv);
            propertyBathNoTv = (TextView)itemView.findViewById(R.id.property_baths_no_tv);
            propertySqftNoTv = (TextView)itemView.findViewById(R.id.property_sqft_no_tv);
        }

        void bindData(final Property list){
            propertyIv.setBackgroundResource(R.drawable.house1);
            propertyPriceTv.setText(list.getProperty_price_tv());
            propertyAddressLine1Tv.setText(list.getProperty_address_line1_tv());
            propertyAddressLine2Tv.setText(list.getProperty_address_line2_tv());
            propertyBedNoTv.setText(list.getProperty_bed_no_tv());
            propertyBathNoTv.setText(list.getProperty_bath_no_tv());
            propertySqftNoTv.setText(list.getProperty_sqft_no_tv());

            propertyIv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"Intending to call PropertyDEtail");
//                    Intent detailPage = new Intent(context,PropertyDetail.class);
//                    detailPage.setClass(context,PropertyDetail.class);
//                    startActivity(detailPage);

                    Context context = v.getContext();
                    Intent intent = new Intent(context, PropertyDetail.class);
                    intent.setClass(context,PropertyDetail.class);
                    context.startActivity(intent);

                }
            });


        }


    }

}
