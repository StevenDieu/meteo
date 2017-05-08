package com.fges.meteo.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fges.meteo.ui.fragment.DayFragment;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class DayAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private static final Integer DAYS_BEFORE = 7;
    private static final Integer DAYS_AFTER = 7;

    private Class<DayFragment> mViewClazz;
    private HashMap mPageReferenceMap = new HashMap();

    public DayAdapter(final FragmentManager fm, final Class<DayFragment> viewClazz) {
        super(fm);
        mViewClazz = viewClazz;
    }

    @Override
    public Fragment getItem(final int i) {
        final Integer daysDiff = DAYS_BEFORE - i;


        Date itemDate = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(itemDate);
        calendar.add(Calendar.DATE, - daysDiff);
        itemDate = calendar.getTime();
        Fragment fragment = null;

        try {
            fragment = mViewClazz.newInstance();
            final Bundle args = new Bundle();
            args.putString(mViewClazz.getName(), String.valueOf(itemDate.getTime()));
            mPageReferenceMap.put(i, fragment);
            fragment.setArguments(args);
        } catch (Exception ex) {
            System.err.println("Exception in " + this.getClass().getName() + " class: " + ex.getMessage());
        }

        return fragment;
    }

    public DayFragment getFragment(final int key) {
        return (DayFragment) mPageReferenceMap.get(key);
    }

    @Override
    public int getCount() {
        return (DAYS_BEFORE + DAYS_AFTER + 1);
    }

    public int getCurrentDatePosition() {
        return DAYS_BEFORE;
    }
}
