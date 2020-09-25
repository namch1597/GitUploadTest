package com.example.task.Network;

import android.content.Context;

import com.example.task.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static APIInterface subwayServices(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.getResources().getString(R.string.subway_base_url))
                .build();

        return retrofit.create(APIInterface.class);
    }

    public static APIInterface weatherServices(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.getResources().getString(R.string.weather_base_url))
                .build();

        return retrofit.create(APIInterface.class);
    }

}
