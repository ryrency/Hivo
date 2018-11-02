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

import java.util.ArrayList;

import edu.sjsu.hivo.R;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetail;
import edu.sjsu.hivo.ui.propertydetail.PropertyDetailAdapter;
import edu.sjsu.hivo.ui.propertydetail.PropertyImages;

public class CustomPagerAdapter extends PagerAdapter {
    String TAG = CustomPagerAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Integer> images = new ArrayList<>();
    int myPosition = -1;
    boolean first;
    public CustomPagerAdapter(Context context, ArrayList<Integer>images){
        mContext=context;
        this.images = images;
    }
    public CustomPagerAdapter(Context context, ArrayList<Integer>images, int position){
        mContext=context;
        this.images = images;
        this.myPosition = position;
        this.first=true;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (myPosition != -1 && first==true) {
            position = myPosition;
            first = false;
        }
        position=(position) % images.size();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout;
        layout = (ViewGroup) inflater.inflate(R.layout.detail_images, container,false);

        final ImageView detailIv;

        detailIv = (ImageView)layout.findViewById(R.id.detail_images_iv);
        if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Bitmap myImg = BitmapFactory.decodeResource(mContext.getResources(),images.get(position));
            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                    matrix, true);
            detailIv.setImageBitmap(rotated);
        }
        else
            detailIv.setBackgroundResource(images.get(position));

        final int finalPosition = position;
        if (mContext instanceof PropertyDetail) {
            detailIv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent imagesScreen = new Intent(mContext, PropertyImages.class);
                    imagesScreen.setClass(mContext, PropertyImages.class);
                    imagesScreen.putIntegerArrayListExtra("LIST", images);
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
        Log.i(TAG,"Images.size "+images.size());
        return Integer.MAX_VALUE;
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
