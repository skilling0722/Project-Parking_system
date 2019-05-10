package com.example.parkingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/*
어플리케이션 시작시 로딩화면
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);            // 실제 사용할 메인 액티비티
        startActivity(intent);
        finish();
    }
}
