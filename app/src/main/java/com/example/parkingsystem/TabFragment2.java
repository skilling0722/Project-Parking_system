package com.example.parkingsystem;

/*
 * 시간대별 데이터에 대한 분석
 * */

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TabFragment2 extends BaseFragment {
    @BindView(R.id.day_usage_barchart) BarChart day_usage_barchart;
    @BindView(R.id.title) TextView tv_title;
    private String spot;
    private String start_date;
    private String end_date;

    private OnFragmentInteractionListener mListener;

    public TabFragment2() {
        // Required empty public constructor
    }

    public static TabFragment2 newInstance(String spot, String start_date, String end_date) {
        TabFragment2 fragment = new TabFragment2();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        ButterKnife.bind(this, view);

        Data_analysis_assistant analysis_assistant = new Data_analysis_assistant();
        Integer[] date_arr = analysis_assistant.date_swap(Integer.parseInt(start_date), Integer.parseInt(end_date));
        tv_title.setText(date_arr[0] + " ~ " + date_arr[1] + " 시간대별 분석");


        try {
            analysis_assistant.day_usage_analysis(new Data_analysis_assistant.Callback_day_usage() {
                @Override
                public void onCallback_day_usage(HashMap<Integer, Integer> map) {
                    ArrayList<BarEntry> barData = new ArrayList<>();

                    TreeMap<Integer, Integer> for_sort = new TreeMap<>(map);       //맵 정렬을 위해 트리맵 사용
                    Iterator<Integer> iterator_key = for_sort.keySet().iterator(); //키값 기준 오름차순 정렬

                    try {
                        for (Integer key : map.keySet()) {
//                            Log.d("testt", "가져온 day_map: " + key + "  " + map.get(key));
                            barData.add(new BarEntry(key, map.get(key)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "input data to chart from map fail");
                    }

                    try {
                        Draw_chart bar_chart = new Draw_chart();
                        bar_chart.setBarData(barData);
                        bar_chart.setBarChart(day_usage_barchart);
                        bar_chart.Draw_barchart();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "day_usage_chart draw fail");
                    }
                }
            }, spot, Integer.parseInt(start_date), Integer.parseInt(end_date));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "day_usage_analysis fail");
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
