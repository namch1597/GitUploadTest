package com.example.task.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.task.R;

import java.util.ArrayList;

public class WeatherViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> datas;

    public WeatherViewPagerAdapter(Context context, ArrayList<String>datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size() * 3;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_weather, container, false);

            TextView textView = (TextView) view.findViewById(R.id.tv_weather_item) ;
            String content = "";
            if (position >= datas.size() * 2) {
                content = datas.get((position-1) % 2);
            } else if (position >= datas.size()) {
                content = datas.get(position - datas.size());
            } else {
                content = datas.get(position);
            }
            textView.setText(content) ;
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // position 값을 받아 주어진 위치의 페이지를 삭제한다
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
