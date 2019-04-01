package com.example.parkingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class spot_check extends AppCompatActivity {

    @BindView(R.id.spot_check_name) TextView spot_check_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_check);
        ButterKnife.bind(this);

        try {
            Intent intent = getIntent();
            spot_check_name.setText(intent.getStringExtra("spot") + " 주차장");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "intent getString fail");
        }
    }


}
