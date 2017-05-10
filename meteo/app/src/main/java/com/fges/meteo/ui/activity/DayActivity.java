package com.fges.meteo.ui.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fges.meteo.R;
import com.fges.meteo.ui.TrackGPS;
import com.fges.meteo.ui.adapter.DayAdapter;
import com.fges.meteo.ui.fragment.DayFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DayActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 200;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
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
    @BindView(R.id.linear_header_text)
    LinearLayout mLinearHeaderText;
    @BindView(R.id.swipe_refresh_weather)
    SwipeRefreshLayout mSwipeRefreshWeather;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.linear_humidity)
    LinearLayout mLinearHumidity;
    @BindView(R.id.linear_wind)
    LinearLayout mLinearWind;
    private double mLongitude;
    private double mLatitude;
    private Context mContext;
    private DayAdapter mDayAdapter;
    private boolean isLoadFirst = true;
    private boolean isOnRefresh = false;
    private boolean mCanRefreshData = false;
    private AlertDialog alertDialogGpsSetting;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);

        mContext = this;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            generateFragment();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    /***************************** MANAGE GPS  *********************************/

    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateFragment();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                        alertDialogGpsSetting =new AlertDialog.Builder(this)
                                .setTitle(R.string.gps)
                                .setMessage(R.string.gps_is_neccesary_go_to_setting)
                                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 0);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return;
                    }else{
                        createAlertGps();
                    }
                }
                break;
            }

        }


    }

    private void createAlertGps() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.gps)
                .setMessage(R.string.gps_is_neccesary)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(DayActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /***************************** GENERATE DATA  *********************************/

    private void generateFragment() {
        final TrackGPS gps = new TrackGPS(this);
        if (gps.isCanGetLocation()) {
            if (alertDialogGpsSetting != null)
                alertDialogGpsSetting.hide();
            mLongitude = gps.getLongitude();
            mLatitude = gps.getLatitude();

            mSwipeRefreshWeather.setOnRefreshListener(this);

            mDayAdapter = new DayAdapter<>(this.getSupportFragmentManager(), DayFragment.class);
            mViewPager.setAdapter(mDayAdapter);
            mViewPager.setCurrentItem(mDayAdapter.getCurrentDatePosition());

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    showProgressBarAndHideImage();
                    final DayFragment dayFragment = mDayAdapter.getFragment(mViewPager.getCurrentItem());
                    if (dayFragment.isDataApiWeatherIsLoad()) {
                        changeDataHeader(dayFragment);
                    } else {
                        if (dayFragment.isLoad()) {
                            dayFragment.setIsNeedToPushOnActivity(true);
                        } else {
                            hideProgressBarAndShowDate();
                            mTextDate.setText(dayFragment.getmTextDate());
                            mSwipeRefreshWeather.setRefreshing(true);

                            dayFragment.setIsNeedToPushOnActivity(true);
                            dayFragment.onRefresh();
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.gps)
                    .setMessage(R.string.no_fournisseur_gps)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            generateFragment();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void changeDataHeader(DayFragment dayFragment) {
        mTextHumidity.setText(dayFragment.getmTextHumidity());
        mTextWind.setText(dayFragment.getmTextWind());
        mTextTemperature.setText(dayFragment.getmTextTemperature());
        mTextDate.setText(dayFragment.getmTextDate());
        if (dayFragment.getmImageWeather() != 0) {
            Picasso.with(mContext).load(dayFragment.getmImageWeather()).into(mImageWeather);
        }
        hideProgressBarAndShowImage();
    }

    private void hideProgressBarAndShowImage() {
        mImageWeather.setVisibility(View.VISIBLE);
        mLinearHeaderText.setVisibility(View.VISIBLE);
        mLinearHumidity.setVisibility(View.VISIBLE);
        mLinearWind.setVisibility(View.VISIBLE);
        mTextTemperature.setVisibility(View.VISIBLE);
        mProgressBarWeather.setVisibility(View.GONE);
        mCanRefreshData = true;
    }

    private void showProgressBarAndHideImage() {
        mCanRefreshData = false;
        mImageWeather.setVisibility(View.GONE);
        mLinearHeaderText.setVisibility(View.INVISIBLE);
        mProgressBarWeather.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarAndShowDate() {
        mImageWeather.setVisibility(View.GONE);
        mLinearHeaderText.setVisibility(View.VISIBLE);
        mProgressBarWeather.setVisibility(View.GONE);
        mLinearHumidity.setVisibility(View.INVISIBLE);
        mLinearWind.setVisibility(View.INVISIBLE);
        mTextTemperature.setVisibility(View.INVISIBLE);
        mCanRefreshData = true;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setIsLoadFirst(boolean isLoadFirst) {
        this.isLoadFirst = isLoadFirst;
    }

    public boolean isLoadFirst() {
        return isLoadFirst;
    }

    /***************************** REFRESH DATA  *********************************/

    @Override
    public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
        if (verticalOffset < 0 && !isOnRefresh) {
            mSwipeRefreshWeather.setEnabled(false);
        } else {
            mSwipeRefreshWeather.setEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onRefresh() {
        if (mCanRefreshData) {
            isOnRefresh = true;
            final DayFragment dayFragment = mDayAdapter.getFragment(mViewPager.getCurrentItem());
            dayFragment.setIsNeedToPushOnActivity(true);
            dayFragment.onRefresh();
        } else {
            mSwipeRefreshWeather.setRefreshing(false);
        }
    }

    public void hideSwipeRefresh() {
        mSwipeRefreshWeather.setRefreshing(false);
        isOnRefresh = false;
    }

    /***************************** NETWORK  *********************************/

    public boolean isConnected() {
        final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }


}