package com.fges.meteo.data.manager;

import android.content.Context;

import com.fges.meteo.data.helper.DatabaseHelper;
import com.fges.meteo.data.model.InformationWeatherCurrentData;

import java.sql.SQLException;

/**
 * Created by Maxime on 16/12/2016.
 */

public class DatabaseManagerInformationWeatherCurrentData {

    static private DatabaseManagerInformationWeatherCurrentData instance;
    private DatabaseHelper helper;

    private DatabaseManagerInformationWeatherCurrentData(final Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    static public DatabaseManagerInformationWeatherCurrentData getInstance(final Context ctx) {
        if (null == instance) {
            instance = new DatabaseManagerInformationWeatherCurrentData(ctx);
        }
        return instance;
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    /******************** DATA CURRENT ********************/

    public void addInformationWeatherCurrentData(final InformationWeatherCurrentData informationWeatherCurrentData) {
        try {
            getHelper().getInformationWeatherCurrentDataDao().createOrUpdate(informationWeatherCurrentData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllInformationWeatherCurrentData() {
        try {
            getHelper().getInformationWeatherCurrentDataDao().deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public InformationWeatherCurrentData getInfomationWeatherForToday(final long beginTime, final long endTime) {
        try {
            return getHelper().getInformationWeatherCurrentDataDao()
                    .queryBuilder()
                    .where().between("time", beginTime, endTime)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
