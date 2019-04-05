package com.example.parkingsystem;

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
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

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
    @BindView(R.id.chart1)
    LineChart linechart;
    @BindView(R.id.chart2) LineChart linechart2;

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
    /*butterknife.bind error..... */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
        ButterKnife.bind(this, view);
        try {
            View_linechart_DB();
            View_linechart();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "draw fail");
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


    private void View_linechart() {
        ArrayList<Entry> vals = new ArrayList<Entry>();

        for (int i = 0; i < 100; i++) {
            float val = (float) (Math.random() );
            vals.add(new Entry(i, val));
        }

        LineDataSet linetype = new LineDataSet(vals, "응가1");
        linetype.setLineWidth(1);
        linetype.setCircleRadius(3);
        linetype.setCircleColor(Color.parseColor("#FFA1BC4A"));

        XAxis xAxis = linechart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextColor(ContextCompat.getColor("#FFA1BC4A");

        YAxis yaxisleft = linechart2.getAxisLeft();
//        yaxisleft.setTextColor(ContextCompat.getColor(getContent));

        YAxis yaxisright = linechart2.getAxisRight();
        yaxisright.setDrawLabels(false);
        yaxisright.setDrawAxisLine(false);
        yaxisright.setDrawGridLines(false);

        LineData lineData = new LineData(linetype);

        linechart2.setData(lineData);
    }

    private void View_linechart_DB() {
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

                XAxis xAxis = linechart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                YAxis yaxisright = linechart.getAxisRight();
                yaxisright.setDrawLabels(false);
                yaxisright.setDrawAxisLine(false);
                yaxisright.setDrawGridLines(false);

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
