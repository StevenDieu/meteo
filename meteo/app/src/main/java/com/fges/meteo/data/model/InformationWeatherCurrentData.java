package com.fges.meteo.data.model;

/**
 * Created by steven on 18/04/2017.
 */

public class InformationWeatherCurrentData implements IIformationWeatherData {
    private String summary;
    private String icon;
    private double temperature;
    private double humidity;
    private double windSpeed;

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public double getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperatureFahrenheitToCelsium() {
        this.temperature = (this.temperature - 32) * 5 / 9;
    }

    @Override
    public double getHumidity() {
        return humidity * 100;
    }

    @Override
    public double getWindSpeed() {
        return windSpeed;
    }

}
