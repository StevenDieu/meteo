package com.fges.meteo.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fges.meteo.R;
import com.fges.meteo.data.manager.WeatherManager;
import com.fges.meteo.data.model.IIformationWeatherData;
import com.fges.meteo.data.model.InformationWeatherCurrentData;
import com.fges.meteo.data.model.InformationWeatherDailyData;
import com.fges.meteo.data.model.WeatherFormServer;
import com.fges.meteo.ui.activity.DayActivity;
import com.fges.meteo.ui.adapter.InformationWeatherAdapter;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by steven on 17/04/2017.
 */

public class DayFragment extends Fragment {
    @BindView(R.id.text_date)
    TextView mTextDate;

    @BindView(R.id.text_humidity)
    TextView mTextHumidity;

    @BindView(R.id.text_wind)
    TextView mTextWind;

    @BindView(R.id.text_temperature)
    TextView mTextTemperature;

    @BindView(R.id.image_weather)
    ImageView mImageWeather;

    @BindView(R.id.progress_bar_weather)
    ProgressBar mProgressBarWeather;

    @BindView(R.id.recycler_information_weather)
    RecyclerView mRecyclerInformationWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        ButterKnife.bind(this, rootView);


        final Context context = getActivity().getApplicationContext();

        final Bundle args = getArguments();

        final String dateFragmentStr = args.getString(this.getClass().getName());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        final LocalDate dateFragment = DateTimeFormat.fullDate().parseLocalDate(dateFragmentStr);
        final LocalDate dateToday = new LocalDate();

        final WeatherManager weatherManager = WeatherManager.getInstance();

        weatherManager.setLatitude(((DayActivity) getActivity()).getLatitude());
        weatherManager.setLongitude(((DayActivity) getActivity()).getLongitude());
        weatherManager.setDateTime(dateFragment.toDate().getTime());

        final boolean isToday = dateToday.equals(dateFragment);
        Typeface robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

        mTextTemperature.setTypeface(robotoLight);
        mTextDate.setTypeface(robotoLight);
        mTextHumidity.setTypeface(robotoLight);
        mTextWind.setTypeface(robotoLight);

        if (isToday) {
            mTextDate.setText("Maintenant");
        } else {
            mTextDate.setText(simpleDateFormat.format(dateFragment.toDate()));
        }

        getInformationWeather(context, weatherManager, isToday);

        return rootView;
    }

    private void getInformationWeather(final Context context, WeatherManager weatherManager, final boolean isToday) {
        weatherManager.getWeather(new MyCallback() {
            @Override
            public WeatherFormServer onSuccess(WeatherFormServer weather) {
                mImageWeather.setVisibility(View.VISIBLE);
                mProgressBarWeather.setVisibility(View.GONE);


                IIformationWeatherData informationWeather = null;
                final ArrayList<Map<String, String>> informationWeatherList = new ArrayList<>();
                final String title = "title";
                final String description = "description";

                if (isToday) {
                    final InformationWeatherCurrentData informationWeatherCurrentData = weather.getCurrently();
                    informationWeather = informationWeatherCurrentData;
                    insetInformationWeatherCurrentlyToList(informationWeatherCurrentData, informationWeatherList);
                } else {
                    if (weather.getDaily() != null && weather.getDaily().getData().size() >= 0) {
                        final InformationWeatherDailyData informationWeatherDailyData = weather.getDaily().getData().get(0);
                        informationWeather = informationWeatherDailyData;
                        insetInformationWeatherDailyToList(informationWeatherDailyData, informationWeatherList);
                    }
                }

                if (informationWeather != null) {

                    InformationWeatherAdapter wodAdapter = new InformationWeatherAdapter(informationWeatherList, context);
                    mRecyclerInformationWeather.setAdapter(wodAdapter);
                    mRecyclerInformationWeather.setLayoutManager(new LinearLayoutManager(context));
                    RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
                    mRecyclerInformationWeather.addItemDecoration(itemDecoration);

                    if (isAdded()) {
                        Picasso.with(context).load(getResources().getIdentifier("weather_" + informationWeather.getIcon().replaceAll("-", "_"), "drawable", context.getPackageName())).into(mImageWeather);
                    }

                    final NumberFormat formatFragionDitisZero = getNumberFormatDigitZero();
                    informationWeather.setTemperatureFahrenheitToCelsium();

                    mTextHumidity.setText(formatFragionDitisZero.format(informationWeather.getPrecipProbability() * 100) + "%");
                    mTextWind.setText(formatFragionDitisZero.format(informationWeather.getWindSpeed()) + " mph");
                    mTextTemperature.setText(formatFragionDitisZero.format(informationWeather.getTemperature()) + "°");
                }

                return weather;
            }

            @Override
            public void onError(String error) {
                mTextDate.setText(error);
            }
        });
    }

    @NonNull
    private NumberFormat getNumberFormatDigitZero() {
        final NumberFormat formatFragionDigitZero = NumberFormat.getInstance();
        formatFragionDigitZero.setMaximumFractionDigits(0);
        formatFragionDigitZero.setMinimumFractionDigits(0);
        return formatFragionDigitZero;
    }

    @NonNull
    private NumberFormat getNumberFormatDigitTwo() {
        final NumberFormat formatFragionDigitZero = NumberFormat.getInstance();
        formatFragionDigitZero.setMaximumFractionDigits(2);
        formatFragionDigitZero.setMinimumFractionDigits(2);
        return formatFragionDigitZero;
    }

    private void insetInformationWeatherDailyToList(final InformationWeatherDailyData informationWeather, final ArrayList<Map<String, String>> informationWeatherList) {
        final NumberFormat formatFragionDitisZero = getNumberFormatDigitZero();
        final NumberFormat formatFragionDitisTwo = getNumberFormatDigitTwo();

        insertInformationWeatherCommonCurentlyAndDaily(informationWeather, informationWeatherList, " dans la journée");
        addMapInformationWeatherToList("Couverture nuageuse dans la journée", formatFragionDitisZero.format(informationWeather.getCloudCover() * 100) + "% de couverture des nuages", informationWeatherList);

    }

    private void insetInformationWeatherCurrentlyToList(final InformationWeatherCurrentData informationWeather, final ArrayList<Map<String, String>> informationWeatherList) {
        final NumberFormat formatFragionDitisZero = getNumberFormatDigitZero();
        final NumberFormat formatFragionDitisTwo = getNumberFormatDigitTwo();

        addMapInformationWeatherToList("Heure", getHoursNow(), informationWeatherList);
        insertInformationWeatherCommonCurentlyAndDaily(informationWeather, informationWeatherList, " maintenant");

    }

    private void insertInformationWeatherCommonCurentlyAndDaily(final IIformationWeatherData informationWeather, final ArrayList<Map<String, String>> informationWeatherList, final String messageAfterTitle) {
        final NumberFormat formatFragionDitisZero = getNumberFormatDigitZero();
        final NumberFormat formatFragionDitisTwo = getNumberFormatDigitTwo();

        addMapInformationWeatherToList("Description", informationWeather.getSummary(), informationWeatherList);
        addMapInformationWeatherToList("Position", "Ma position", informationWeatherList);
        addMapInformationWeatherToList("Probabilité qu'il pleuve" + messageAfterTitle, formatFragionDitisZero.format(informationWeather.getPrecipProbability() * 100) + "%", informationWeatherList);
        if (informationWeather.getPrecipProbability() > 0d) {
            addMapInformationWeatherToList("Intensité de la pluie" + messageAfterTitle, formatFragionDitisTwo.format(informationWeather.getPrecipIntensity() * 25.4) + " Millimètre / Heure", informationWeatherList);
        }
        addMapInformationWeatherToList("La pression de l'air" + messageAfterTitle, formatFragionDitisZero.format(informationWeather.getPressure()) + " millibars", informationWeatherList);
        addMapInformationWeatherToList("Mesure de la masse surfacique de l'ozone atmosphérique" + messageAfterTitle, formatFragionDitisTwo.format(informationWeather.getOzone()) + " DU", informationWeatherList);
    }

    private String getHoursNow() {
        final Date date = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.FRENCH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return sdf.format(date);
    }

    private void addMapInformationWeatherToList(final String title, final String description, final ArrayList<Map<String, String>> informationWeatherList) {
        final Map<String, String> mapInformationWeather = new HashMap<>();
        mapInformationWeather.put("title", title);
        mapInformationWeather.put("description", description);
        informationWeatherList.add(mapInformationWeather);
    }

    public interface MyCallback {
        WeatherFormServer onSuccess(WeatherFormServer listCategory);

        void onError(String error);
    }

}
