package com.fges.meteo.data.model;

/**
 * Created by steven on 19/04/2017.
 */

public interface IIformationWeatherData {

    String getSummary();

    String getIcon();

    double getTemperature();

    /**
     * Celsium = (Fahrenheit - 32) * 5 / 9
     */
    void setTemperatureFahrenheitToCelsium();

    double getHumidity();

    double getWindSpeed();
}
