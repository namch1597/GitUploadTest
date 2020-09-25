package com.example.task.Network;

import com.example.task.Model.SubwayModel.DirectionResponses;
import com.example.task.Model.WeatherModel.WeatherResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("maps/api/directions/json")
    Call<DirectionResponses> getDirection(@Query("origin") String origin,
                                          @Query("destination") String destination,
                                          @Query("mode") String MODE ,
                                          @Query("key") String apiKey);

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(@Query("lat") String lat,
                                @Query("lon") String lon,
                                @Query("APPID") String APPID);

}
