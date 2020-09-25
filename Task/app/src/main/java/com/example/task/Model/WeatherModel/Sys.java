package com.example.task.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("country")
    private String country;
    @SerializedName("sunrise")
    Long sunrise = 0L;
    @SerializedName("sunset")
    Long sunset = 0L;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }
}
