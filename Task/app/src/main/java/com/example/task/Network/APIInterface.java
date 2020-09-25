package com.example.task.Network;

import com.example.task.Model.DirectionResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("maps/api/directions/json")
    Call<DirectionResponses> getDirection(@Query("origin") String origin,
                                          @Query("destination") String destination,
                                          @Query("mode") String MODE ,
                                          @Query("key") String apiKey);

}
