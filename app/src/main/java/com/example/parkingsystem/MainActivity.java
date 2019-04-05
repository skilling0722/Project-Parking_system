package com.example.parkingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
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
    @BindView(R.id.firebase_test) Button firebase_test_btn;

    @OnClick(R.id.button1)
    void  button1Click() {
        Intent intent = new Intent(this, parking_check.class);
        startActivity(intent);
    }


    @OnClick(R.id.button2)
    void buttonClick2() {
  //      Intent intent = new Intent(this, test.class);
//        startActivity(intent);
        Intent intent = new Intent(this, Data_analysis_Activity.class);
        startActivity(intent);

    }

    @OnClick(R.id.button3)
    void buttonClick3() {
        Intent intent = new Intent(this, Data_analysis.class);
        startActivity(intent);
    }

    @OnClick(R.id.firebase_test)
    void buttonClick4() {
        Intent intent = new Intent(this, DB_test.class);
        startActivity(intent);
    }
}
