package com.fges.meteo.data.model;

/**
 * Created by steven on 17/04/2017.
 */

public class WeatherFormServer {

    private String latitude;
    private String longitude;
    private String timezone;
    private InformationWeatherCurrentData currently;
    private InformationWeatherDaily daily;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public InformationWeatherCurrentData getCurrently() {
        return currently;
    }

    public InformationWeatherDaily getDaily() {
        return daily;
    }

    public void setDaily(InformationWeatherDaily daily) {
        this.daily = daily;
    }
}
