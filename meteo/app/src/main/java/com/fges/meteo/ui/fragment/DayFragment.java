package com.fges.meteo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fges.meteo.R;
import com.fges.meteo.data.manager.WeatherManager;
import com.fges.meteo.data.model.WeatherFormServer;
import com.fges.meteo.ui.activity.DayActivity;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

/**
 * Created by steven on 17/04/2017.
 */

public class DayFragment extends Fragment {
    @BindView(R.id.textTest)
    TextView mTextView;

    @BindView(R.id.weather)
    WeatherView mWeatherView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        ButterKnife.bind(this, rootView);

        final Bundle args = getArguments();
        final String dateTimeFragmentStr = args.getString(this.getClass().getName());
        final LocalDate dateTimeFragment = DateTimeFormat.fullDate().parseLocalDate(dateTimeFragmentStr);
        final WeatherManager weatherManager = WeatherManager.getInstance();




        mWeatherView.startAnimation();

        weatherManager.setLatitude(((DayActivity) getActivity()).getLatitude());
        weatherManager.setLongitude(((DayActivity) getActivity()).getLongitude());
        weatherManager.setDateTime(dateTimeFragment.toDate().getTime());
        weatherManager.getWeather(new MyCallback() {
            @Override
            public WeatherFormServer onSuccess(WeatherFormServer weather) {
                mTextView.setText(weather.getTimezone());
                weather.getCurrently();
                return weather;
            }

            @Override
            public void onError(String error) {
                mTextView.setText(error);
            }
        });

        return rootView;
    }

    public interface MyCallback {
        WeatherFormServer onSuccess(WeatherFormServer listCategory);
        void onError(String error);
    }

}
