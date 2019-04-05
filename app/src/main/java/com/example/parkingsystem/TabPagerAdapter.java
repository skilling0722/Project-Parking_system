package com.example.parkingsystem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.mikephil.charting.charts.LineChart;

import butterknife.BindView;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_NUMBER = 3;

    //test code
    @BindView(R.id.chart1)
    LineChart linechart;
    @BindView(R.id.chart2) LineChart linechart2;
    public TabPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                return TabFragment1.newInstance("1","1");
            case 1:
                return TabFragment2.newInstance("1","1");
            case 2:
                return TabFragment3.newInstance("1","1");
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "first Tap";
            case 1:
                return "second Tap";
            case 2:
                return "third Tap";
            default:
                return null;
        }
    }
}
