package com.fges.meteo.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fges.meteo.data.model.InformationWeatherCurrentData;
import com.fges.meteo.data.model.InformationWeatherDailyData;
import com.fges.meteo.ui.adapter.InformationWeatherAdapter;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Maxime on 14/12/2016.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "weather";
    private static final int    DATABASE_VERSION = 1;

    private Dao<InformationWeatherCurrentData, Integer> mWodDAO = null;
    private Dao<InformationWeatherDailyData, Integer> mMouvementDAO = null;

    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, InformationWeatherCurrentData.class);
            TableUtils.createTable(connectionSource, InformationWeatherDailyData.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, InformationWeatherCurrentData.class, true);
            TableUtils.dropTable(connectionSource, InformationWeatherDailyData.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<InformationWeatherCurrentData, Integer> getWodDao() throws SQLException {
        if (mWodDAO == null) {
            mWodDAO = getDao(InformationWeatherCurrentData.class);
        }
        return mWodDAO;
    }

    public Dao<InformationWeatherDailyData, Integer> getMouvementDao() throws SQLException {
        if (mMouvementDAO == null) {
            mMouvementDAO = getDao(InformationWeatherDailyData.class);
        }
        return mMouvementDAO;
    }

    /********** Mouvement part end **********/

    public void clearDatabase(){
        mContext.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void close() {
        mWodDAO = null;
        mMouvementDAO = null;

        super.close();
    }
}