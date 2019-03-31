package com.example.parkingsystem;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        try {
            View_linechart_DB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "draw fail");
        }
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

    private void View_linechart_DB() {
        final LineChart linechart = (LineChart) findViewById(R.id.chart1);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").child("a").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> yData = new ArrayList<>();
                float i = 0;
                int count = 0;

                try {
                    Log.d("testt", "View_linechart_DB start");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        i++;

                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
                        Log.d("testt", "read data: " + data.getDate() + ", " + data.getTime() + ", " + data.isUse());
                        if (data.isUse()){
                            count ++;
                        }

                        yData.add(new Entry(i, count));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "View_linechart_DB fail");
                }

                Log.d("testt", "i의 값: " + i);
                Log.d("testt", "count의 값: "+count);

                LineDataSet line_dataset = new LineDataSet(yData, "응가");
                LineData data = new LineData(line_dataset);
                linechart.setData(data);
                linechart.notifyDataSetChanged();
                linechart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
