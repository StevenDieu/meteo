package com.fges.meteo.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fges.meteo.data.model.InformationWeatherCurrentData;
import com.fges.meteo.data.model.InformationWeatherDailyData;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Maxime on 14/12/2016.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "weather";
    private static final int DATABASE_VERSION = 1;

    private Dao<InformationWeatherCurrentData, Integer> mInformationWeatherCurrentDataDAO = null;
    private Dao<InformationWeatherDailyData, Integer> mInformationWeatherDailyDataDAO = null;

    private Context mContext;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, InformationWeatherCurrentData.class);
            TableUtils.createTable(connectionSource, InformationWeatherDailyData.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource,
                          final int oldVersion, final int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, InformationWeatherCurrentData.class, true);
            TableUtils.dropTable(connectionSource, InformationWeatherDailyData.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<InformationWeatherCurrentData, Integer> getInformationWeatherCurrentDataDao() throws SQLException {
        if (mInformationWeatherCurrentDataDAO == null) {
            mInformationWeatherCurrentDataDAO = getDao(InformationWeatherCurrentData.class);
        }
        return mInformationWeatherCurrentDataDAO;
    }

    public Dao<InformationWeatherDailyData, Integer> getInformationWeatherDailyDataDao() throws SQLException {
        if (mInformationWeatherDailyDataDAO == null) {
            mInformationWeatherDailyDataDAO = getDao(InformationWeatherDailyData.class);
        }
        return mInformationWeatherDailyDataDAO;
    }

    @Override
    public void close() {
        mInformationWeatherCurrentDataDAO = null;
        mInformationWeatherDailyDataDAO = null;

        super.close();
    }
}