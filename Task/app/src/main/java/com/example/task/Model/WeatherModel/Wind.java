package com.example.task.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    Float speed = 0.0f;
    @SerializedName("deg")
    Float deg = 0.0f;

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getDeg() {
        return deg;
    }

    public void setDeg(Float deg) {
        this.deg = deg;
    }
}
