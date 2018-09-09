package edu.sjsu.hivo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import edu.sjsu.hivo.R;

public class CustomPagerAdapter extends PagerAdapter {
    String TAG = CustomPagerAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Integer> images = new ArrayList<>();
    public CustomPagerAdapter(Context context, ArrayList<Integer>images){
        mContext=context;
        this.images = images;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i(TAG,"Instantiating new Item");
        Log.i(TAG, "Position is"+position);

        position=(position)%images.size();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.detail_images,container,false);
        final ImageView detailIv;

        detailIv = (ImageView)layout.findViewById(R.id.detail_images_iv);
        detailIv.setBackgroundResource(images.get(position));

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
