package com.example.task.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("weather")
    ArrayList<Weather> weather;
    @SerializedName("main")
    Main main;
    @SerializedName("wind")
    Wind wind;
    @SerializedName("sys")
    Sys sys;

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
