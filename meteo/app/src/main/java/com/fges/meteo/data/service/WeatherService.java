package com.fges.meteo.data.service;

import com.fges.meteo.data.model.WeatherFormServer;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by steven on 17/04/2017.
 */

public interface WeatherService {
    @GET("forecast")
    Call<WeatherFormServer> getWeather();
}
