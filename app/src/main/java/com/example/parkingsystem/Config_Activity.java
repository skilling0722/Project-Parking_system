package com.example.parkingsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
* SettingPreferenceFragment(이벤트 제어, 셋팅), configs.xml(리스트 정의), settings_preference.xml(세팅화면 레이아웃)
* activity_config_.xml에 제어클래스를 등록해주면 된다.
* */
public class Config_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*미리 그리기*/
        setBackground(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_);

    }

    public static String setBackground(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String isTheme = prefs.getString("background_list", "검은 색");
        if(isTheme.equals("검은 색")){
            context.setTheme(R.style.blackTheme);
        }
        else if(isTheme.equals("빨간 색")){
            context.setTheme(R.style.redTheme);
        }
        else if(isTheme.equals("연한 녹색")){
            context.setTheme(R.style.greenTheme);
        }else{
            context.setTheme(R.style.blueTheme);
        }

        return isTheme;
    }
}
