package com.example.ser.navigationactivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



public class ViewPagerAdapter extends PagerAdapter {
   private Context context;
    private LayoutInflater inflater;
    ViewPager viewPager;
    private Integer[] image={R.drawable.indian,R.drawable.tea,R.drawable.southindian};
    public ViewPagerAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.customlayout,null);
        ImageView imageview1=(ImageView)view.findViewById(R.id.imageview1);
        imageview1.setImageResource(image[position]);
        ViewPager viewPager=(ViewPager)container;
        viewPager.addView(view,0);
        return(view);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager=(ViewPager)container;
        View view=(View)object;
        viewPager.removeView(view);
    }
}
