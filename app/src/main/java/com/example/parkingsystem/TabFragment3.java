package com.example.parkingsystem;

/*
 * 요일별 데이터에 대한 분석
 * */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment3 extends Fragment {
    @BindView(R.id.week_usage_horizontalbarchart) HorizontalBarChart week_usage_horizontalbarchart;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;

    public TabFragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment3 newInstance(String param1, String param2) {
        TabFragment3 fragment = new TabFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment3, container, false);
        ButterKnife.bind(this, view);

        Data_analysis_assistant analysis_assistant = new Data_analysis_assistant();

//        Log.d("testt", "요일별 분석 프래그먼트 시작");
        try {
            analysis_assistant.week_usage_analysis(new Data_analysis_assistant.Callback_week_usage() {
                @Override
                public void onCallback_week_usage(HashMap<Integer, Integer> map) {
//                    Log.d("testt", "요일별 분석 시작");
                    ArrayList<BarEntry> barData = new ArrayList<>();

                    TreeMap<Integer, Integer> for_sort = new TreeMap<>(map);       //맵 정렬을 위해 트리맵 사용
                    Iterator<Integer> iterator_key = for_sort.keySet().iterator(); //키값 기준 오름차순 정렬

                    try {
                        for (Integer key : map.keySet()) {
//                            Log.d("testt", "가져온 week_map: " + key + "   " + map.get(key));
                            barData.add(new BarEntry(key, map.get(key)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "input data to chart from map fail");
                    }

                    try {
                        Draw_chart horizontalbar_chart = new Draw_chart();
                        horizontalbar_chart.setBarData(barData);
                        horizontalbar_chart.setHorizontalbarchart(week_usage_horizontalbarchart);
                        horizontalbar_chart.Draw_horizontalbarchart();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "draw fail");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "week_usage_analysis fail");
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
