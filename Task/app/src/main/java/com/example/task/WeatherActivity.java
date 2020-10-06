package com.example.task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.task.Adapter.WeatherViewPagerAdapter;
import com.example.task.Model.WeatherModel.WeatherResponse;
import com.example.task.Network.APIClient;
import com.example.task.Network.APIInterface;
import com.example.task.Utils.GpsTracker;
import com.example.task.databinding.ActivityWeatherBinding;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener  {

    ActivityWeatherBinding binding;

    GpsTracker gpsTracker;

    double latitude = 0.0f;
    double longitude = 0.0f;

    ArrayList<String>datas = new ArrayList<String>();

    WeatherResponse weatherResponse;
    String realJuso;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 3000;
    final long TIME_MS = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        FirstViewSetting();

    }

    public void FirstViewSetting() {
        Toolbar toolbar = (Toolbar) binding.tbToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        gpsTracker = new GpsTracker(this);

        latitude = gpsTracker.getLatitude(); // 위도
        longitude = gpsTracker.getLongitude(); //경도

        APIInterface weatherService = APIClient.weatherServices(this);
        binding.pbCenter.setVisibility(View.VISIBLE);
        weatherService.getWeather(Double.toString(latitude),Double.toString(longitude),"e78e9675b61b45f7c19b03ebbaaa130f")  .enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                weatherResponse = response.body();

                String icon = weatherResponse.getWeather().get(0).getIcon();
                String iconUrl = "https://openweathermap.org/img/w/" + icon + ".png";
                String weather = transferWeather(weatherResponse.getWeather().get(0).getDescription()); //날씨
                String humidity = Float.toString(weatherResponse.getMain().getHumidity()); //습도
                String speed = Float.toString(weatherResponse.getWind().getSpeed());    // 풍속
                String nowTemp = Float.toString((float) (((1.8 * (weatherResponse.getMain().getTemp() - 273.15) + 32) - 32) / 1.8) );      //현재온도
                String minTemp = Float.toString((float) (((1.8 * (weatherResponse.getMain().getTemp_min() - 273.15) + 32) - 32) / 1.8) );   //최저온도
                String maxTemp = Float.toString((float) (((1.8 * (weatherResponse.getMain().getTemp_max() - 273.15) + 32) - 32) / 1.8) );   //최고온도

                SimpleDateFormat format2 = new SimpleDateFormat ( "MM월dd일 HH시mm분");
                String format_time2 = format2.format (System.currentTimeMillis());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        realJuso = getCurrentAddress(latitude,longitude,"").getAddressLine(0);
                        Glide.with(WeatherActivity.this)
                                .load(iconUrl)
                                .override(200, 200)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        binding.tvNowLocation.setText(realJuso);
                                        String message = weather + " 습도 : " + humidity + "풍속 : " + speed + "m/s" + "현재 온도 :"
                                                + nowTemp + "최저온도 : " + minTemp + "최고온도 : " + maxTemp;
                                        binding.tvWeather.setText(weather);
                                        binding.tvNowTemp.setText(nowTemp.substring(0,2) + "°C");
                                        binding.tvMinMaxTemp.setText("최고" + maxTemp.substring(0,2) + " °C/ 최저" + minTemp.substring(0,2) + "°C");
                                        binding.tvNow.setText(format_time2);
                                        binding.pbCenter.setVisibility(View.INVISIBLE);

                                        datas.add("습도 : " + humidity);
                                        datas.add("풍속 : " + speed + "m/s");
                                        datas.add("최고 : " + maxTemp.substring(0,2) + " °C");
                                        datas.add("최저 : " + minTemp.substring(0,2) + "°C");
                                        setupViewPager(binding.viewpager,datas);

                                        return false;
                                    }
                                }).into(binding.ivIcon);
                    }
                });

            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.e("js error", t.getLocalizedMessage());
            }
        });
    }

    private void setupViewPager(ViewPager viewPager,ArrayList<String> datas) {

        WeatherViewPagerAdapter weatherViewPagerAdapter = new WeatherViewPagerAdapter(WeatherActivity.this,datas);
        viewPager.setAdapter(weatherViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position < datas.size()) {
                    currentPage = position + datas.size();
                    viewPager.setCurrentItem(currentPage, false);
                } else if (position >= datas.size()*2) {
                    currentPage = position - datas.size();
                    viewPager.setCurrentItem(currentPage, false);
                } else {
                    currentPage = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Handler handler = new Handler();
        Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 0) {
                    currentPage = datas.size() + 1;
                } else if (currentPage >= datas.size()*2) {
                    currentPage = datas.size();
                }
                viewPager.setCurrentItem(currentPage, false);
                currentPage++;
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, TIME_MS);

    }

    private String transferWeather(String weather) {

        weather = weather.toLowerCase();
        String result = "알수없음";

        if ("haze".equals(weather)) {
            return "안개";
        } else if ("fog".equals(weather)) {
            return "안개";
        } else if ("clouds".equals(weather)) {
            return "구름";
        } else if ("few clouds".equals(weather)) {
            return "구름 조금";
        } else if ("scattered clouds".equals(weather)) {
            return "구름 낌";
        } else if ("broken clouds".equals(weather)) {
            return "구름 많음";
        } else if ("overcast clouds".equals(weather)) {
            return "맑음";
        } else if ("clear sky".equals(weather)) {
            return "안개";
        }

        return result;

    }

    public Address getCurrentAddress(double latitude, double longitude, String juso) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            // 0.0f 주소로 변환
            if (latitude == 0.0f) {
                addresses = geocoder.getFromLocationName( juso, 3);
            } else {
                addresses = geocoder.getFromLocation( latitude, longitude, 3);
            }
        } catch (IOException ioException) {
            //네트워크 문제

        } catch (IllegalArgumentException illegalArgumentException) {

        }
        if (addresses == null || addresses.size() == 0) {

        }
        Address address = addresses.get(0);
        return address;
        //여기부터는 GPS 활성화를 위한 메소드들
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}