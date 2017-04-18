package com.fges.meteo.data.manager;

import android.util.Log;

import com.fges.meteo.data.ServiceGenerator;
import com.fges.meteo.data.model.WeatherFormServer;
import com.fges.meteo.data.service.WeatherService;
import com.fges.meteo.ui.fragment.DayFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by steven on 17/04/2017.
 */

public class WeatherManager {
    private static final WeatherManager instance = new WeatherManager();
    private Double mLatitude;
    private Double mLongitude;
    private long mDateTime;
    private WeatherManager() {
    }

    public static WeatherManager getInstance() {
        return instance;
    }

    public void getWeather(final DayFragment.MyCallback myCallback) {

        final WeatherService weatherService = ServiceGenerator.createService(WeatherService.class, mLatitude, mLongitude, mDateTime);

        final Call<WeatherFormServer> call = weatherService.getWeather();

        call.enqueue(new Callback<WeatherFormServer>() {
            @Override
            public void onResponse(Call<WeatherFormServer> call, Response<WeatherFormServer> response) {

                if (response.isSuccessful()) {
                    final WeatherFormServer weatherFormServer = response.body();

                    Log.d("weather", weatherFormServer.getTimezone());

                    myCallback.onSuccess(weatherFormServer);

                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<WeatherFormServer> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public void setDateTime(long dateTime) {
        this.mDateTime = dateTime;
    }
}
