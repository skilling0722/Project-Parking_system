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
        //TabPagerAdapter setting -> TabFragement1,2,3 등록
        TabPagerAdapter adapter =  new TabPagerAdapter(getSupportFragmentManager());
        //탭레이아웃에 추가 3번째param에 1은 값넘기는 것.
        adapter.add(R.drawable.ic_launcher, "one", TabFragment3.newInstance("1","1"));
        adapter.add(R.drawable.ic_launcher, "two", TabFragment3.newInstance("1","1"));
        adapter.add(R.drawable.ic_launcher, "three", TabFragment3.newInstance("1","1"));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout viewPagerTab = (TabLayout) findViewById(R.id.tabs);
        viewPagerTab.setupWithViewPager(viewPager);

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            //아이콘 가져와서 고정시킨다.
            viewPagerTab.getTabAt(i).setIcon(adapter.getFragmentInfo(i).getIconResId());
        }
    }
}
