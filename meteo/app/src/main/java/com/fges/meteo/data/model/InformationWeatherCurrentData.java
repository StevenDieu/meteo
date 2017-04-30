package com.fges.meteo.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by steven on 18/04/2017.
 */

@DatabaseTable(tableName = "information_weather_current")
public class InformationWeatherCurrentData implements IIformationWeatherData {
    @DatabaseField(generatedId = true)
    private int time;

    @DatabaseField(canBeNull = false)
    private String summary;
    private String icon;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private double precipIntensity;
    private double precipProbability;
    private double cloudCover;
    private double pressure;
    private double ozone;

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

    @Override
    public double getPressure() {
        return pressure;
    }

    @Override
    public double getOzone() {
        return ozone;
    }
}
