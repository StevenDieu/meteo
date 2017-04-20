package com.fges.meteo.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fges.meteo.R;
import com.fges.meteo.data.manager.WeatherManager;
import com.fges.meteo.data.model.IIformationWeatherData;
import com.fges.meteo.data.model.WeatherFormServer;
import com.fges.meteo.ui.activity.DayActivity;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by steven on 17/04/2017.
 */

public class DayFragment extends Fragment {
    @BindView(R.id.text_date)
    TextView mTextDate;

    @BindView(R.id.text_summary)
    TextView mTextSummary;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        ButterKnife.bind(this, rootView);


        final Context context = getActivity().getApplicationContext();

        final Bundle args = getArguments();

        final String dateFragmentStr = args.getString(this.getClass().getName());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

        final LocalDate dateFragment = DateTimeFormat.fullDate().parseLocalDate(dateFragmentStr);
        final LocalDate dateToday = new LocalDate();

        final WeatherManager weatherManager = WeatherManager.getInstance();

        weatherManager.setLatitude(((DayActivity) getActivity()).getLatitude());
        weatherManager.setLongitude(((DayActivity) getActivity()).getLongitude());
        weatherManager.setDateTime(dateFragment.toDate().getTime());

        final boolean isToday = dateToday.equals(dateFragment);
        Typeface robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

        mTextTemperature.setTypeface(robotoLight);
        mTextSummary.setTypeface(robotoLight);
        mTextDate.setTypeface(robotoLight);
        mTextHumidity.setTypeface(robotoLight);
        mTextWind.setTypeface(robotoLight);

        if (isToday) {
            mTextDate.setText("Ajourd'hui");
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
                if (isToday) {
                    informationWeather = weather.getCurrently();
                } else {
                    if (weather.getDaily() != null && weather.getDaily().getData().size() >= 0) {
                        informationWeather = weather.getDaily().getData().get(0);
                    }
                }

                if (informationWeather != null) {

                    if (isAdded()) {
                        Picasso.with(context).load(getResources().getIdentifier("weather_" + informationWeather.getIcon().replaceAll("-", "_"), "drawable", context.getPackageName())).into(mImageWeather);
                    }

                    final NumberFormat formatFragionDitisZero = NumberFormat.getInstance();
                    formatFragionDitisZero.setMaximumFractionDigits(0);
                    formatFragionDitisZero.setMinimumFractionDigits(0);
                    informationWeather.setTemperatureFahrenheitToCelsium();

                    mTextSummary.setText(informationWeather.getSummary());
                    mTextHumidity.setText(formatFragionDitisZero.format(informationWeather.getHumidity()) + "%");
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


    public interface MyCallback {
        WeatherFormServer onSuccess(WeatherFormServer listCategory);

        void onError(String error);
    }

}
