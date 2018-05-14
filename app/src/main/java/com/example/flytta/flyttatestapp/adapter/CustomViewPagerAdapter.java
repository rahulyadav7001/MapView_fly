package com.example.flytta.flyttatestapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.flytta.flyttatestapp.R;

import java.util.List;

/**
 * Created by WIN 1O on 18-03-2018.
 */

public class CustomViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> imageList;

    public CustomViewPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(List<String> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        if (imageList != null && imageList.size() > 0)
            return imageList.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pics);
        Glide.with(mContext)
                .load(imageList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_image_available)
                .into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
