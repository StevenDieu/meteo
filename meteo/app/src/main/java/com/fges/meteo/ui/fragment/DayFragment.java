package com.fges.meteo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fges.meteo.R;
import com.fges.meteo.data.manager.DatabaseManagerInformationWeatherCurrentData;
import com.fges.meteo.data.manager.DatabaseManagerInformationWeatherDailyData;
import com.fges.meteo.data.manager.WeatherManager;
import com.fges.meteo.data.model.IIformationWeatherData;
import com.fges.meteo.data.model.InformationWeatherCurrentData;
import com.fges.meteo.data.model.InformationWeatherDailyData;
import com.fges.meteo.data.model.WeatherFormServer;
import com.fges.meteo.ui.activity.DayActivity;
import com.fges.meteo.ui.adapter.InformationWeatherAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by steven.
 */
public class DayFragment extends Fragment {


    @BindView(R.id.recycler_information_weather)
    RecyclerView mRecyclerInformationWeather;
    @BindView(R.id.text_alert_bdd)
    TextView mTextAlertBdd;
    @BindView(R.id.text_no_data)
    TextView mTextNoData;

    private DayActivity mDayActivity;
    private DayFragment mThis;
    private Context mApplicationContext;
    private Context mContext;
    private String mTextHumidity;
    private String mTextWind;
    private String mTextTemperature;
    private String mTextDate;
    private int mImageWeather;
    private Date mDateFragment;
    private boolean mIsRefresh;
    private boolean isDataApiWeatherIsLoad = false;
    private boolean isNeedToPushOnActivity = false;
    private boolean isLoad = true;
    private boolean isToday;

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        ButterKnife.bind(this, rootView);

        final RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecyclerInformationWeather.addItemDecoration(itemDecoration);

        onCreateViewWeather();

        return rootView;
    }

    private void onCreateViewWeather() {
        mApplicationContext = getActivity().getApplicationContext();
        mContext = getContext();
        mDayActivity = (DayActivity) getActivity();
        mThis = this;

        final String dateFragmentStr = getArguments().getString(DayFragment.class.getName());
        mDateFragment = new Date(Long.parseLong(dateFragmentStr));

        isToday = trimDate(new Date()).equals(trimDate(mDateFragment));

        if (isToday) {
            mTextDate = getString(R.string.now);
        } else {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_format_dd_MMMM_yyyy), Locale.FRENCH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getString(R.string.time_zone_paris)));
            mTextDate = simpleDateFormat.format(mDateFragment);
        }

        if (mDayActivity.isConnected()) {
            callApiToInjectInformationWeather();
        } else {
            callBddToInjectInformationWeather();
        }
    }

    /****************************** GET DATA API ******************************************/

    private void callApiToInjectInformationWeather() {
        final WeatherManager weatherManager = WeatherManager.getInstance();

        final long dateCallApi;
        if (isToday) {
            dateCallApi = new Date().getTime();
        } else {
            dateCallApi = trimDate(mDateFragment).getTime();
        }

        weatherManager.getWeather(new MyCallback() {
            @Override
            public WeatherFormServer onSuccess(WeatherFormServer weather) {
                mTextAlertBdd.setVisibility(View.GONE);
                mTextNoData.setVisibility(View.GONE);
                isLoad = true;

                IIformationWeatherData informationWeather = null;
                final ArrayList<Map<String, String>> informationWeatherList = new ArrayList<>();

                if (isAdded()) {
                    if (isToday) {
                        informationWeather = callApiToInjectCurrentInformationWeather(weather, informationWeatherList);
                    } else {
                        if (weather.getDaily() != null && weather.getDaily().getData().size() >= 0) {
                            informationWeather = callApiToInjectDailtyInformationWeather(weather, informationWeatherList, dateCallApi);
                        }
                    }
                }

                injectInformationWeather(informationWeather, informationWeatherList);
                return weather;
            }

            @Override
            public void onError() {
                callBddToInjectInformationWeather();
            }
        }, mDayActivity.getmLatitude(), mDayActivity.getmLongitude(), dateCallApi);
    }

    private IIformationWeatherData callApiToInjectDailtyInformationWeather(final WeatherFormServer weather, final ArrayList<Map<String, String>> informationWeatherList, final long dateCallApi) {
        IIformationWeatherData informationWeather;
        final InformationWeatherDailyData informationWeatherDailyData = weather.getDaily().getData().get(0);
        informationWeather = informationWeatherDailyData;
        informationWeatherDailyData.setTime(dateCallApi / 1000L);
        insertInformationWeatherDailyToList(informationWeatherDailyData, informationWeatherList);
        DatabaseManagerInformationWeatherDailyData.getInstance().addInformationWeatherDailyData(informationWeatherDailyData);
        return informationWeather;
    }

    private IIformationWeatherData callApiToInjectCurrentInformationWeather(final WeatherFormServer weather, final ArrayList<Map<String, String>> informationWeatherList) {
        IIformationWeatherData informationWeather;
        final InformationWeatherCurrentData informationWeatherCurrentData = weather.getCurrently();
        informationWeather = informationWeatherCurrentData;
        insertInformationWeatherCurrentlyToList(informationWeatherCurrentData, informationWeatherList);
        DatabaseManagerInformationWeatherCurrentData.getInstance().addInformationWeatherCurrentData(informationWeatherCurrentData);
        return informationWeather;
    }

    /****************************** GET DATA BDD ******************************************/

    private void callBddToInjectInformationWeather() {
        if (isToday) {
            callBddToInjectCurrentInformationWeather();
        } else {
            callBddToInjectDailyInformationWeather();
        }
    }

    private void callBddToInjectCurrentInformationWeather() {
        final DatabaseManagerInformationWeatherCurrentData dManagerCurrent = DatabaseManagerInformationWeatherCurrentData.getInstance();
        final InformationWeatherCurrentData informationWeatherCurrentData = dManagerCurrent.getInfomationWeatherForToday(trimDate(mDateFragment).getTime() / 1000L, trimDate(addDay(mDateFragment)).getTime() / 1000L);

        if (informationWeatherCurrentData != null) {
            final ArrayList<Map<String, String>> informationWeatherList = new ArrayList<>();

            mTextAlertBdd.setVisibility(View.VISIBLE);
            isLoad = true;
            mTextDate = getString(R.string.today);

            insertInformationWeatherCurrentlyToList(informationWeatherCurrentData, informationWeatherList);
            injectInformationWeather(informationWeatherCurrentData, informationWeatherList);
        } else {
            callBddToInjectDailyInformationWeather();
        }
    }

    private void callBddToInjectDailyInformationWeather() {
        final DatabaseManagerInformationWeatherDailyData dManagerDaily = DatabaseManagerInformationWeatherDailyData.getInstance();
        final InformationWeatherDailyData informationWeatherDailyData = dManagerDaily.getInformationWeatherForDay(trimDate(mDateFragment).getTime() / 1000L);

        if (informationWeatherDailyData != null) {
            final ArrayList<Map<String, String>> informationWeatherList = new ArrayList<>();

            isLoad = true;
            mTextAlertBdd.setVisibility(View.VISIBLE);

            insertInformationWeatherDailyToList(informationWeatherDailyData, informationWeatherList);
            injectInformationWeather(informationWeatherDailyData, informationWeatherList);
        } else {
            mTextNoData.setVisibility(View.VISIBLE);
            isLoad = false;
            if (mIsRefresh) {
                mIsRefresh = false;
                mDayActivity.hideSwipeRefresh();
            }
        }
    }

    /********************************** REFRESH DATA  *************************************/

    public void onRefresh() {
        mIsRefresh = true;
        onCreateViewWeather();
    }

    /****************************** INJECT DATA WEATHER ***********************************/

    private void injectInformationWeather(final IIformationWeatherData informationWeather, final ArrayList<Map<String, String>> informationWeatherList) {
        if (informationWeather != null) {
            if (isAdded()) {
                mImageWeather = getResources().getIdentifier(getString(R.string.prefix_weather) + informationWeather.getIcon().replaceAll("-", "_"), getString(R.string.drawable), mApplicationContext.getPackageName());
            }

            final NumberFormat formatFragionDitisZero = getNumberFormatDigitZero();
            informationWeather.setTemperatureFahrenheitToCelsium();
            mTextHumidity = formatFragionDitisZero.format(informationWeather.getPrecipProbability() * 100) + getString(R.string.pourcente);
            mTextWind = formatFragionDitisZero.format(informationWeather.getWindSpeed()) + getString(R.string.mph);
            mTextTemperature = formatFragionDitisZero.format(informationWeather.getTemperature()) + getString(R.string.degres);
            isDataApiWeatherIsLoad = true;

            final InformationWeatherAdapter informationWeatherAdapter = new InformationWeatherAdapter(informationWeatherList, mContext);
            mRecyclerInformationWeather.setAdapter(informationWeatherAdapter);
            mRecyclerInformationWeather.setLayoutManager(new LinearLayoutManager(mContext));

            if (isNeedToPushOnActivity) {
                mDayActivity.changeDataHeader(mThis);
                if (mIsRefresh) {
                    mIsRefresh = false;
                    mDayActivity.hideSwipeRefresh();
                }
            } else if (mDayActivity.isLoadFirst() && isToday) {
                mDayActivity.setIsLoadFirst(false);
                mDayActivity.changeDataHeader(mThis);
            }
        }
    }

    private void insertInformationWeatherCurrentlyToList(final InformationWeatherCurrentData informationWeather, final ArrayList<Map<String, String>> informationWeatherList) {
        addMapInformationWeatherToList(getString(R.string.hours), getHoursApi(informationWeather.getTime()), informationWeatherList);
        insertInformationWeatherCommonCurentlyAndDaily(informationWeather, informationWeatherList, getString(R.string.suffix_now));
    }

    private void insertInformationWeatherDailyToList(final InformationWeatherDailyData informationWeather, final ArrayList<Map<String, String>> informationWeatherList) {
        final NumberFormat formatFragionDitisZero = getNumberFormatDigitZero();

        insertInformationWeatherCommonCurentlyAndDaily(informationWeather, informationWeatherList, getString(R.string.suffix_durring_the_day));
        addMapInformationWeatherToList(getString(R.string.cloud_cover_day), formatFragionDitisZero.format(informationWeather.getCloudCover() * 100) + getString(R.string.pourcente_cloud_cover), informationWeatherList);

    }

    private void insertInformationWeatherCommonCurentlyAndDaily(final IIformationWeatherData informationWeather, final ArrayList<Map<String, String>> informationWeatherList, final String messageAfterTitle) {
        final NumberFormat formatFragionDitisZero = getNumberFormatDigitZero();
        final NumberFormat formatFragionDitisTwo = getNumberFormatDigitTwo();

        addMapInformationWeatherToList(getString(R.string.prefix_description), informationWeather.getSummary(), informationWeatherList);
        addMapInformationWeatherToList(getString(R.string.prefix_position), getString(R.string.my_position), informationWeatherList);
        addMapInformationWeatherToList(getString(R.string.prefix_probability_rain) + messageAfterTitle, formatFragionDitisZero.format(informationWeather.getPrecipProbability() * 100) + getString(R.string.pourcente), informationWeatherList);
        if (informationWeather.getPrecipProbability() > 0d) {
            addMapInformationWeatherToList(getString(R.string.prefix_intensity_rain) + messageAfterTitle, formatFragionDitisTwo.format(informationWeather.getPrecipIntensity() * 25.4) + getString(R.string.millimeter_by_hour), informationWeatherList);
        }
        addMapInformationWeatherToList(getString(R.string.prefix_pressure_air) + messageAfterTitle, formatFragionDitisZero.format(informationWeather.getPressure()) + getString(R.string.millibars), informationWeatherList);
        addMapInformationWeatherToList(getString(R.string.prefixe_ozone) + messageAfterTitle, formatFragionDitisTwo.format(informationWeather.getOzone()) + getString(R.string.du), informationWeatherList);
    }

    private void addMapInformationWeatherToList(final String title, final String description, final ArrayList<Map<String, String>> informationWeatherList) {
        final Map<String, String> mapInformationWeather = new HashMap<>();
        mapInformationWeather.put(getString(R.string.title), title);
        mapInformationWeather.put(getString(R.string.description), description);
        informationWeatherList.add(mapInformationWeather);
    }

    private NumberFormat getNumberFormatDigitZero() {
        final NumberFormat formatFragionDigitZero = NumberFormat.getInstance();
        formatFragionDigitZero.setMaximumFractionDigits(0);
        formatFragionDigitZero.setMinimumFractionDigits(0);
        return formatFragionDigitZero;
    }

    private NumberFormat getNumberFormatDigitTwo() {
        final NumberFormat formatFragionDigitZero = NumberFormat.getInstance();
        formatFragionDigitZero.setMaximumFractionDigits(2);
        formatFragionDigitZero.setMinimumFractionDigits(2);
        return formatFragionDigitZero;
    }

    private String getHoursApi(Long time) {
        final Date dateApi = new Date();
        dateApi.setTime(time * 1000L);
        final SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format_HH_mm), Locale.FRENCH);
        sdf.setTimeZone(TimeZone.getTimeZone(getString(R.string.time_zone_paris)));
        return sdf.format(dateApi);
    }

    public Date trimDate(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    private Date addDay(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);

        return calendar.getTime();
    }

    public boolean isDataApiWeatherIsLoad() {
        return isDataApiWeatherIsLoad;
    }

    public void setIsNeedToPushOnActivity(final boolean isNeedToPushOnActivity) {
        this.isNeedToPushOnActivity = isNeedToPushOnActivity;
    }

    public String getmTextHumidity() {
        return mTextHumidity;
    }

    public String getmTextWind() {
        return mTextWind;
    }

    public String getmTextTemperature() {
        return mTextTemperature;
    }

    public String getmTextDate() {
        return mTextDate;
    }

    public int getmImageWeather() {
        return mImageWeather;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public interface MyCallback {
        WeatherFormServer onSuccess(final WeatherFormServer listCategory);

        void onError();
    }

}
