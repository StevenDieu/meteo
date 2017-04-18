package com.fges.meteo.ui.activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fges.meteo.R;
import com.fges.meteo.ui.TrackGPS;
import com.fges.meteo.ui.adapter.GenericDayAdapter;
import com.fges.meteo.ui.fragment.DayFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 200;

    private static double longitude;
    private static double latitude;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    Context mContext;

    public static double getLongitude() {
        return longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);
        mContext = this;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final TrackGPS gps = new TrackGPS(this);
            generateFragmentWithGps(gps);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final TrackGPS gps = new TrackGPS(this);
                    generateFragmentWithGps(gps);
                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("GPS")
                            .setMessage("Il est nécessaire d'utiliser le GPS pour cette application")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(DayActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            }

        }
    }

    private void generateFragmentWithGps(TrackGPS gps) {
        longitude = gps.getLongitude();
        latitude = gps.getLatitude();

        final GenericDayAdapter mDayAdapter = new GenericDayAdapter<>(this.getSupportFragmentManager(), DayFragment.class);

        mViewPager.setAdapter(mDayAdapter);
        mViewPager.setCurrentItem(mDayAdapter.getCurrentDatePosition());
    }
}