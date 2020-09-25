package com.example.task.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    Float temp = 0.0f;
    @SerializedName("humidity")
    Float humidity = 0.0f;
    @SerializedName("pressure")
    Float pressure = 0.0f;
    @SerializedName("temp_min")
    Float temp_min = 0.0f;
    @SerializedName("temp_max")
    Float temp_max = 0.0f;

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(Float temp_min) {
        this.temp_min = temp_min;
    }

    public Float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(Float temp_max) {
        this.temp_max = temp_max;
    }
}
