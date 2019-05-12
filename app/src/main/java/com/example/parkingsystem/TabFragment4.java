package com.example.parkingsystem;

/*
    월별 분석
 */


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TabFragment4 extends BaseFragment {
    @BindView(R.id.piechart) PieChart piechart;
    @BindView(R.id.title) TextView tv_title;
    private OnFragmentInteractionListener mListener;
    private String spot;
    private String start_date;
    private String end_date;

    public TabFragment4() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    
    public static TabFragment4 newInstance(String spot, String start_date, String end_date) {
        TabFragment4 fragment = new TabFragment4();
        Bundle args = new Bundle();
        args.putString("spot", spot);
        args.putString("start_date", start_date);
        args.putString("end_date", end_date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null) {
            spot = args.getString("spot", "default spot");
            start_date = args.getString("start_date", "default start_date");
            end_date = args.getString("end_date", "default end_date");
        }
    }

    @Override
    public void onStart() {
//        show_loading();
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment4, container, false);
        /* xml 변수 가져와 바인딩 */
        ButterKnife.bind(this, view);
        /* 오잉? */
        Data_analysis_assistant analysis_assistant = new Data_analysis_assistant();
        Integer[] date_arr = analysis_assistant.date_swap(Integer.parseInt(start_date), Integer.parseInt(end_date));
        tv_title.setText(date_arr[0] + " ~ " + date_arr[1]);



        try {
            analysis_assistant.weather_usage_analysis(new Data_analysis_assistant.Callback_weather_usage() {
                @Override
                public void onCallback_weather_usage(HashMap<String, Integer> map) {
//                    Log.d("testt", "날짜별 분석 시작");
                    ArrayList<PieEntry> pieData = new ArrayList<PieEntry>();

                    //TreeMap<String, Integer> for_sort = new TreeMap<>(map);       //맵 정렬을 위해 트리맵 사용
                    //Iterator<Integer> iterator_key = for_sort.keySet().iterator(); //키값 기준 오름차순 정렬

                    try {
                        for (String key : map.keySet()) {
//                            Log.d("testt", "가져온 week_map: " + key + "   " + map.get(key));
                            if(map.get(key) >0){
                                pieData.add(new PieEntry((float)(map.get(key)), key));   // 값, 라벨
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "input data to chart from map fail");
                    }

                    try {
                        Draw_chart pi_chart = new Draw_chart();
                        pi_chart.setPieData(pieData);
                        pi_chart.setPieChart(piechart);
                        pi_chart.Draw_piechart("날씨별 이용수", "날씨");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "draw fail");
                    }
                }
            }, spot, Integer.parseInt(start_date), Integer.parseInt(end_date));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "weather_usage_analysis fail");
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
