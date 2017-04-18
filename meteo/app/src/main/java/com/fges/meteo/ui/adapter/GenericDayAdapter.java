package com.fges.meteo.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;


public class GenericDayAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private static final Integer DAYS_BEFORE = 7;
    private static final Integer DAYS_AFTER = 7;

    private Class<T> mViewClazz;

    public GenericDayAdapter(final FragmentManager fm,final Class<T> viewClazz) {
        super(fm);
        mViewClazz = viewClazz;
    }

    @Override
    public Fragment getItem(final int i) {
        final Integer daysDiff = DAYS_BEFORE - i;
        final LocalDate itemDate = LocalDate.now().minusDays(daysDiff);
        Fragment fragment = null;

        try {
            fragment = mViewClazz.newInstance();
            final Bundle args = new Bundle();
            args.putString(mViewClazz.getName(), itemDate.toString(DateTimeFormat.fullDate()));
            fragment.setArguments(args);
        } catch (Exception ex) {
            System.err.println("Exception in " + this.getClass().getName() + " class: " + ex.getMessage());
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return (DAYS_BEFORE + DAYS_AFTER + 1);
    }

    public int getCurrentDatePosition() {
        return DAYS_BEFORE;
    }
}
