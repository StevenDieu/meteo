package com.fges.meteo.data.manager;

import android.util.Log;

import com.fges.meteo.data.ServiceGenerator;
import com.fges.meteo.data.model.WeatherFormServer;
import com.fges.meteo.data.service.WeatherService;
import com.fges.meteo.ui.activity.DayActivity;
import com.fges.meteo.ui.fragment.DayFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by steven on 17/04/2017.
 */

public class WeatherManager {
    private static final WeatherManager instance = new WeatherManager();

    private WeatherManager() {
    }

    public static WeatherManager getInstance() {
        return instance;
    }

    public void getWeather(final DayFragment.MyCallback myCallback,Double latitude,Double longitude,long dateTime) {

        ServiceGenerator serviceGenerator = new ServiceGenerator();
        final WeatherService weatherService = serviceGenerator.createService(WeatherService.class, latitude, longitude, dateTime);

        final Call<WeatherFormServer> call = weatherService.getWeather();

        call.enqueue(new Callback<WeatherFormServer>() {
            @Override
            public void onResponse(Call<WeatherFormServer> call, Response<WeatherFormServer> response) {

                if (response.isSuccessful()) {
                    final WeatherFormServer weatherFormServer = response.body();

                    Log.d("weather", weatherFormServer.getTimezone());

                    myCallback.onSuccess(weatherFormServer);

                } else {
                    myCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<WeatherFormServer> call, Throwable t) {
                myCallback.onError();
            }
        });
    }

}
