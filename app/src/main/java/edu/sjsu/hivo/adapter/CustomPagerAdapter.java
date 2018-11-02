package edu.sjsu.hivo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetail;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetailAdapter;
import edu.sjsu.hivo.ui.propertydetail.PropertyImages;

public class CustomPagerAdapter extends PagerAdapter {
    String TAG = CustomPagerAdapter.class.getSimpleName();
    private Context mContext;
    private List<String> images = new ArrayList<>();

    public CustomPagerAdapter(Context context, List<String> images){
        mContext=context;
        this.images = images;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        position=(position) % images.size();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout;
        layout = (ViewGroup) inflater.inflate(R.layout.detail_images, container,false);

        final ImageView detailIv = (ImageView)layout.findViewById(R.id.detail_images_iv);
        RequestOptions options = new RequestOptions();
        options = options.centerCrop();
        Glide.with(mContext).load(images.get(position)).apply(options).into(detailIv);

        final int finalPosition = position;
        if (mContext instanceof PropertyDetail) {
            detailIv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent imagesScreen = new Intent(mContext, PropertyImages.class);
                    imagesScreen.putExtra("POSITION", finalPosition);
                    mContext.startActivity(imagesScreen);

                }
            });
        }
//        if (holder instanceof PortViewHolder)
//        {
//            PortViewHolder portHolder = (PortViewHolder) holder;
//            //Initialize Views here for Port View
//        } else if (holder instanceof LandViewHolder)
//        {
//            LandViewHolder landViewHolder = (LandViewHolder) holder;
//            //Initialize Views here for Land View
//        }
        container.addView(layout);
        return layout;

    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return images.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "House Photos";
    }

}
