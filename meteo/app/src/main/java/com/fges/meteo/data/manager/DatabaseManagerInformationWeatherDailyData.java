package com.fges.meteo.data.manager;

import android.content.Context;

import com.fges.meteo.data.helper.DatabaseHelper;
import com.fges.meteo.data.model.InformationWeatherDailyData;

import java.sql.SQLException;

/**
 * Created by Maxime on 16/12/2016.
 */

public class DatabaseManagerInformationWeatherDailyData {

    static private DatabaseManagerInformationWeatherDailyData instance;
    private DatabaseHelper helper;

    private DatabaseManagerInformationWeatherDailyData(final Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    static public DatabaseManagerInformationWeatherDailyData getInstance(final Context ctx) {
        if (instance == null) {
            instance = new DatabaseManagerInformationWeatherDailyData(ctx);
        }
        return instance;
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public void addInformationWeatherDailyData(final InformationWeatherDailyData informationWeatherDailyData) {
        try {
            getHelper().getInformationWeatherDailyDataDao().createOrUpdate(informationWeatherDailyData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public InformationWeatherDailyData getInformationWeatherForDay(final long time) {
        try {
            return getHelper().getInformationWeatherDailyDataDao()
                    .queryBuilder()
                    .where().eq("time", time)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
