package com.example.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.task.Model.SubwayModel.DirectionResponses;
import com.example.task.Model.WeatherModel.WeatherResponse;
import com.example.task.Network.APIClient;
import com.example.task.Network.APIInterface;
import com.example.task.Utils.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    GpsTracker gpsTracker;

    double latitude = 0.0f;
    double longitude = 0.0f;

    WeatherResponse weatherResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        gpsTracker = new GpsTracker(this);

        latitude = gpsTracker.getLatitude(); // 위도
        longitude = gpsTracker.getLongitude(); //경도

        APIInterface weatherService = APIClient.weatherServices(this);
        System.out.println("---Double.toString(latitude) : " + Double.toString(latitude));
        System.out.println("---Double.toString(longitude) : " + Double.toString(longitude));
        weatherService.getWeather(Double.toString(latitude),Double.toString(longitude),"e78e9675b61b45f7c19b03ebbaaa130f")  .enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                weatherResponse = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.e("js error", t.getLocalizedMessage());
            }
        });

    }
}