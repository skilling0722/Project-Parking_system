package com.example.parkingsystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Data_analysis extends AppCompatActivity {
    @BindView(R.id.month_usage_chart) LineChart month_usage_chart;
    @BindView(R.id.day_usage_chart) PieChart day_usage_chart;
    @BindView(R.id.day_usage_linechart) LineChart day_usage_linechart;
    @BindView(R.id.day_usage_barchart) BarChart day_usage_barchart;
    @BindView(R.id.week_usage_horizontalbarchart) HorizontalBarChart week_usage_horizontalbarchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        ButterKnife.bind(this);

        try {
            month_usage_analysis(new Callback_month_usage() {
                @Override
                public void onCallback_month_usage(HashMap<Integer, Integer> map) {
                    ArrayList<Entry> yData = new ArrayList<>();

                    TreeMap<Integer, Integer> for_sort = new TreeMap<>(map);       //맵 정렬을 위해 트리맵 사용
                    Iterator<Integer> iterator_key = for_sort.keySet().iterator(); //키값 기준 오름차순 정렬

                    try {
                        for (Integer key : map.keySet()) {
//                            Log.d("testt", "가져온 month_map: " + key + "  " + map.get(key));
                            yData.add(new Entry(key, map.get(key)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "input data to chart from map fail");
                    }

                    try {
                        Draw_chart line_chart = new Draw_chart();
                        line_chart.setLineData(yData);
                        line_chart.setLinechart(month_usage_chart);
                        line_chart.Draw_linechart();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "draw fail");
                    }
                }
            }, 2019);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "month_usage_analysis fail");
        }

        try {
            day_usage_analysis(new Callback_day_usage() {
                @Override
                public void onCallback_day_usage(HashMap<Integer, Integer> map) {
                    ArrayList<PieEntry> pieData = new ArrayList<>();
                    ArrayList<Entry> Data = new ArrayList<>();
                    ArrayList<BarEntry> barData = new ArrayList<>();

                    TreeMap<Integer, Integer> for_sort = new TreeMap<>(map);       //맵 정렬을 위해 트리맵 사용
                    Iterator<Integer> iterator_key = for_sort.keySet().iterator(); //키값 기준 오름차순 정렬

                    try {
                        for (Integer key : map.keySet()) {
//                            Log.d("testt", "가져온 day_map: " + key + "  " + map.get(key));
                            pieData.add(new PieEntry(key, map.get(key)));
                            Data.add(new Entry(key, map.get(key)));
                            barData.add(new BarEntry(key, map.get(key)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "input data to chart from map fail");
                    }

                    try {
                        Draw_chart pie_chart = new Draw_chart();
                        pie_chart.setPieData(pieData);
                        pie_chart.setPieChart(day_usage_chart);
                        pie_chart.Draw_piechart();

                        Draw_chart line_chart = new Draw_chart();
                        line_chart.setLineData(Data);
                        line_chart.setLinechart(day_usage_linechart);
                        line_chart.Draw_linechart();

                        Draw_chart bar_chart = new Draw_chart();
                        bar_chart.setBarData(barData);
                        bar_chart.setBarChart(day_usage_barchart);
                        bar_chart.Draw_barchart();

                        Draw_chart horizontalbar_chart = new Draw_chart();
                        horizontalbar_chart.setBarData(barData);
                        horizontalbar_chart.setHorizontalbarchart(week_usage_horizontalbarchart);
                        horizontalbar_chart.Draw_horizontalbarchart();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "day_usage_chart draw fail");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "day_usage_analysis fail");
        }
    }



    public HashMap<Integer, String> get_reg_for_month_analysis(int year) {
        HashMap<Integer, String> map = new HashMap<>();
        for ( int i = 1; i <= 12; i++) {
            if(i < 10) {
                map.put(i, year + "0" + i + "[0-9][0-9]");
            } else {
                map.put(i, year + ""+ i + "[0-9][0-9]");
            }
        }
        return map;
    }


    public HashMap<Integer, Integer> init_month_count_map() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for ( int i = 1; i <= 12; i++){
            map.put(i, 0);
        }
        return map;
    }

    public HashMap<Integer, Integer> init_day_count_map() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for ( int i = 0; i <= 23; i++){
            map.put(i, 0);
        }
        return map;
    }

    public void month_usage_analysis(final  Callback_month_usage callback, int year) {
        final HashMap<Integer, String> reg_month_map = get_reg_for_month_analysis(year);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").child("a").orderByChild("use").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, Integer> month_count = null;
                try {
                    month_count = init_month_count_map();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
                        Log.d("testt", "extract data: " + data.getDate() + ", " + data.getTime() + ", "+data.isUse());

                        for ( int i = 1; i <= 12; i++ ) {
                            if ((""+data.getDate()).matches(reg_month_map.get(i))) {
                                month_count.put(i, month_count.get(i)+1);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "month_usage_analysis get DB fail");
                }

                for ( int i = 1; i <= 12; i++ ) {
                    Log.d("testt", "월: " +  i + ", month_count " +month_count.get(i));
                }
                callback.onCallback_month_usage(month_count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface Callback_month_usage {
        void onCallback_month_usage(HashMap<Integer, Integer> map);
    }

    public void day_usage_analysis(final  Callback_day_usage callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").child("a").orderByChild("use").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, Integer> day_count = null;
                try {
                    day_count = init_day_count_map();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
                        day_count.put(data.getTime(), day_count.get(data.getTime()) + 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "day_usage_analysis get DB fail");
                }

                for ( int i = 0; i <= 23; i++ ) {
                    Log.d("testt", "시간: " +  i + ", day_count " + day_count.get(i));
                }
                callback.onCallback_day_usage(day_count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface Callback_day_usage {
        void onCallback_day_usage(HashMap<Integer, Integer> map);
    }
}
