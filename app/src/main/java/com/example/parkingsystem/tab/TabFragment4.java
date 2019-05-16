package com.example.parkingsystem.tab;

/*
    월별 분석
 */


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkingsystem.BaseFragment;
import com.example.parkingsystem.Data_analysis_assistant;
import com.example.parkingsystem.Draw_MarkerView;
import com.example.parkingsystem.Draw_chart;
import com.example.parkingsystem.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        tv_title.setText(date_arr[0] + " ~ " + date_arr[1] + " 날씨별 분석");



        try {
            analysis_assistant.weather_usage_analysis(new Data_analysis_assistant.Callback_weather_usage() {
                @Override
                public void onCallback_weather_usage(HashMap<String, Integer> map) {
//                    Log.d("testt", "날짜별 분석 시작");
                    ArrayList<PieEntry> pieData = new ArrayList<PieEntry>();

                    /* 정렬 */
                    Iterator sort_val = sortFromVal(map).iterator();
                    try {
                        /* 정렬된 값들을 찾아서 차례대로 add하기 */
                        String key_val = null;
                        while(sort_val.hasNext()){
                            key_val = (String)sort_val.next();
                            if(map.get(key_val) >0){
                                pieData.add(new PieEntry((float)(map.get(key_val)), key_val));   // 값, 라벨
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "input data to chart from map fail");
                    }

                    try {
                        /* pi_chart표에 관한 설정 */
                        Draw_chart pi_chart = new Draw_chart();
                        pi_chart.setPieData(pieData);
                        pi_chart.setPieChart(piechart);

                        /* markerview make */
                        Draw_MarkerView marker = new Draw_MarkerView(getContext() ,R.layout.markerview);
                        marker.setChartView(pi_chart.getPieChart());
                        pi_chart.getPieChart().setMarker(marker);

                        pi_chart.Draw_piechart("날씨별 이용률", "날씨");
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

    /* 정렬에 쓰이는 것 */
    public static List sortFromVal(final Map map){
        List<String> list = new ArrayList<String>();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator(){
            public int compare(Object o1, Object o2){
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v2).compareTo(v1);
            }
        });
        return list;
    }
}
