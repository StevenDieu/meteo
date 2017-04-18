package com.fges.meteo.data.model;

import java.util.Map;

/**
 * Created by steven on 17/04/2017.
 */

public class WeatherFormServer {

    private String latitude;
    private String longitude;
    private String timezone;
    private Map<String,String> currently;

    public WeatherFormServer() {
    }

    public WeatherFormServer(String latitude, String longitude, String timezone, Map<String, String> currently) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.currently = currently;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public Map<String, String> getCurrently() {
        return currently;
    }

    public void setCurrently(Map<String, String> currently) {
        this.currently = currently;
    }
}
