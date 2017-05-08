package com.fges.meteo.data.manager;

import android.content.Context;

import com.fges.meteo.data.helper.DatabaseHelper;
import com.fges.meteo.data.model.InformationWeatherCurrentData;
import com.fges.meteo.data.model.InformationWeatherDaily;
import com.fges.meteo.data.model.InformationWeatherDailyData;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Maxime on 16/12/2016.
 */

public class DatabaseManagerInformationWeatherDailyData {

    static private DatabaseManagerInformationWeatherDailyData instance;

    static public void init(final Context ctx) {
        if (null == instance) {
            instance = new DatabaseManagerInformationWeatherDailyData(ctx);
        }
    }

    static public DatabaseManagerInformationWeatherDailyData getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManagerInformationWeatherDailyData(final Context ctx) {
        helper = new DatabaseHelper(ctx);
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
