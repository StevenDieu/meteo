package com.fges.meteo.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by steven on 18/04/2017.
 */

@DatabaseTable(tableName = "information_weather_daily")
public class InformationWeatherDailyData implements IIformationWeatherData {
    @DatabaseField(id = true)
    private Long time;

    @DatabaseField(canBeNull = false)
    private double temperatureMin;

    @DatabaseField(canBeNull = false)
    private double temperatureMax;

    @DatabaseField(canBeNull = false)
    private String summary;

    @DatabaseField(canBeNull = false)
    private String icon;

    @DatabaseField(canBeNull = false)
    private double humidity;

    @DatabaseField(canBeNull = false)
    private double windSpeed;

    @DatabaseField(canBeNull = false)
    private double precipIntensity;

    @DatabaseField(canBeNull = false)
    private double precipProbability;

    @DatabaseField(canBeNull = false)
    private double cloudCover;

    @DatabaseField(canBeNull = false)
    private double pressure;

    @DatabaseField(canBeNull = false)
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
    public Long getTime() {
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

    public void setTime(Long time) {
        this.time = time;
    }
}
