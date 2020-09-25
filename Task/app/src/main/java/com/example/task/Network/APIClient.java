package com.example.task.Network;

import android.content.Context;

import com.example.task.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static APIInterface apiServices(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.getResources().getString(R.string.base_url))
                .build();

        return retrofit.create(APIInterface.class);
    }

}
