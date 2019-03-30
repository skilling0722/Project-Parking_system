package com.example.parkingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @BindView(R.id.button1) Button btn1;
    @BindView(R.id.button2) Button btn2;
    @BindView(R.id.button3) Button btn3;

    @OnClick(R.id.button1)
    void  button1Click() {
        Intent intent = new Intent(this, parking_check.class);
        startActivity(intent);
    }

    @OnClick(R.id.button2)
    void buttonClick2() {
        Firebase_assist fire_assistant = new Firebase_assist();

        assistant random = new assistant();
        for ( int i = 0; i < 5; i++ ) {
            String num = random.generate_random_num();
            String str = random.generate_random_str();
            HashMap<String, String> Date = random.get_day();

            try {
                fire_assistant.write_db_day_count("parking_spots", str, Date.get("day"), Date.get("time"), num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.button3)
    void buttonClick3() {
        Firebase_assist fire_assistant = new Firebase_assist();
        fire_assistant.get_allcount_from_spot("parking_spots");
    }
}
