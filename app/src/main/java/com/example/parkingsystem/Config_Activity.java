package com.example.parkingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
* SettingPreferenceFragment(이벤트 제어, 셋팅), configs.xml(리스트 정의), settings_preference.xml(세팅화면 레이아웃)
* activity_config_.xml에 제어클래스를 등록해주면 된다.
* */
public class Config_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_);

    }
}
