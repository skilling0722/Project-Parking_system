package com.example.parkingsystem;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class test extends AppCompatActivity {
    @BindView(R.id.chart1) LineChart chart1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        View_linechart();
    }



    private void View_linechart() {
        LineChart linechart = (LineChart) findViewById(R.id.chart1);

        ArrayList<Entry> vals = new ArrayList<Entry>();

        for (int i = 0; i < 100; i++) {
            float val = (float) (Math.random() );
            vals.add(new Entry(i, val));
        }

        LineDataSet linetype = new LineDataSet(vals, "응가1");
        linetype.setLineWidth(1);
        linetype.setCircleRadius(3);
        linetype.setCircleColor(Color.parseColor("#FFA1BC4A"));

        XAxis xAxis = linechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextColor(ContextCompat.getColor("#FFA1BC4A");

        YAxis yaxisleft = linechart.getAxisLeft();
//        yaxisleft.setTextColor(ContextCompat.getColor(getContent));

        YAxis yaxisright = linechart.getAxisRight();
        yaxisright.setDrawLabels(false);
        yaxisright.setDrawAxisLine(false);
        yaxisright.setDrawGridLines(false);

        LineData lineData = new LineData(linetype);

        linechart.setData(lineData);
    }



}
