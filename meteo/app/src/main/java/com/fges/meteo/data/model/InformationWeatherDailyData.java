package com.fges.meteo.data.model;

/**
 * Created by steven on 18/04/2017.
 */

public class InformationWeatherDailyData implements IIformationWeatherData {
    private double temperatureMin;
    private double temperatureMax;
    private String summary;
    private String icon;
    private double humidity;
    private double windSpeed;
    private int time;
    private double precipIntensity;
    private double precipProbability;
    private double cloudCover;
    private double pressure;
    private double ozone;

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

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
        return (this.temperatureMax + this.temperatureMin) / 2d;
    }

    @Override
    public void setTemperatureFahrenheitToCelsium() {
        this.temperatureMax = (this.temperatureMax - 32d) * 5d / 9d;
        this.temperatureMin = (this.temperatureMin - 32d) * 5d / 9d;
    }

    @Override
    public double getHumidity() {
        return humidity * 100;
    }

    @Override
    public double getWindSpeed() {
        return windSpeed;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public double getPrecipIntensity() {
        return precipIntensity;
    }

    @Override
    public double getPrecipProbability() {
        return precipProbability;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    @Override
    public double getPressure() {
        return pressure;
    }

    @Override
    public double getOzone() {
        return ozone;
    }
}
