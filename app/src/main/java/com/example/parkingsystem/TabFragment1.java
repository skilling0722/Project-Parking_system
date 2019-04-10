package com.example.parkingsystem;

/*
* 일별 데이터에 대한 분석
* */

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment1 extends Fragment {
    @BindView(R.id.month_usage_chart) LineChart month_usage_chart;

    private OnFragmentInteractionListener mListener;

    public TabFragment1() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    
    public static TabFragment1 newInstance(String param1, String param2) {
        TabFragment1 fragment = new TabFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
        ButterKnife.bind(this, view);

        Data_analysis_assistant analysis_assistant = new Data_analysis_assistant();
        try {
            analysis_assistant.month_usage_analysis(new Data_analysis_assistant.Callback_month_usage() {
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
