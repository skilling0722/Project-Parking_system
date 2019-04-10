package com.example.parkingsystem;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Data_analysis_Activity extends AppCompatActivity {
    @BindView(R.id.main_analysis_title) TextView tv_main_analysis_title;

    private String spot;
    private String start_date;
    private String end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis_main);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        spot = intent.getStringExtra("spot");
        start_date = intent.getStringExtra("start_date");
        end_date = intent.getStringExtra("end_date");
//        Log.d("testt", "분석 액티비티로 넘어온 값들: " + spot + "   " + start_date + "   " + end_date);
        inittv(spot);



        //TabPagerAdapter setting -> TabFragement1,2,3 등록
        TabPagerAdapter adapter =  new TabPagerAdapter(getSupportFragmentManager());
        //탭레이아웃에 추가 3번째param에 1은 값넘기는 것.
        adapter.add(R.drawable.ic_launcher, "월별", TabFragment1.newInstance(spot, start_date, end_date));
        adapter.add(R.drawable.ic_launcher, "시간대별", TabFragment2.newInstance(spot, start_date, end_date));
        adapter.add(R.drawable.ic_launcher, "요일별", TabFragment3.newInstance(spot, start_date, end_date));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout viewPagerTab = (TabLayout) findViewById(R.id.tabs);
        viewPagerTab.setupWithViewPager(viewPager);

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            //아이콘 가져와서 고정시킨다.
            viewPagerTab.getTabAt(i).setIcon(adapter.getFragmentInfo(i).getIconResId());
        }
    }

    private void inittv(String spot) {
        tv_main_analysis_title.setText(spot + " 주차장 분석");
    }
}
