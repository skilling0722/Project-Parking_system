package com.example.parkingsystem;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Data_analysis_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis_main);

        Log.e("error", "SmartLayout");
        //adapter. connect.
        TabPagerAdapter adapter =  new TabPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout viewPagerTab = (TabLayout) findViewById(R.id.tabs);
        viewPagerTab.setupWithViewPager(viewPager);
    }
}
