package com.example.parkingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.adapter.BaseWheelAdapter;
import com.wx.wheelview.common.WheelData;
import com.wx.wheelview.util.WheelUtils;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class menu_analysis_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.menu_analysis_title) TextView tv_menu_analysis_title;
    @BindView(R.id.pick_parkingspot) Button btn_pick_parkingspot;
    @BindView(R.id.picked_parkingspot) TextView tv_picked_parkingspot;
    @BindView(R.id.pick_date) Button btn_pick_date;
    @BindView(R.id.from_date) TextView tv_from_date;
    @BindView(R.id.to_date) TextView tv_to_date;
    @BindView(R.id.start_analysis) Button btn_start_analysis;
    @BindView(R.id.wheelview) WheelView pick_spot_wheelViewdialog;
    private ArrayList<String> parking_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_analysis);
        ButterKnife.bind(this);

        btn_pick_date.setEnabled(false);
        btn_start_analysis.setEnabled(false);



    }

    @OnClick(R.id.pick_parkingspot)
    void pick_parkingspot() {
        get_parking_list(new Callback_parkinglist() {
            @Override
            public void onCallback_parkinglist(ArrayList<String> arraylist) {
                parking_list = arraylist;
                select_spot_dialog();
            }
        });
    }

    @OnClick(R.id.pick_date)
    void pick_date() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                menu_analysis_Activity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datepickerdialog.setThemeDark(true);
        datepickerdialog.show(getFragmentManager(), "날짜 선택");
    }

    @OnClick(R.id.start_analysis)
    void start_analysis() {
        Intent intent = new Intent(this, Data_analysis_Activity.class);
        startActivity(intent);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String from = year + "   " + monthOfYear + "   " + dayOfMonth;
        String to = yearEnd + "   " + monthOfYearEnd + "   " + dayOfMonthEnd;

        tv_from_date.setText(from);
        tv_to_date.setText(to);

        btn_start_analysis.setEnabled(true);
    }

    private void select_spot_dialog() {
        WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("주차장").setItems(parking_list)
                .setDialogStyle(Color.parseColor("#086A87"))
                .setCount(5);
        dialog.setLoop(true);
        dialog.setTextColor(Color.parseColor("#2EFEF7"));
        dialog.setTextSize(20);
        dialog.setButtonColor(Color.parseColor("#2EFEF7"));
        dialog.setButtonText("확인");
        dialog.show();

        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, String spot) {
//                Log.d("testt", "selected: " + position + "   " + s);
                tv_picked_parkingspot.setText(spot);
                btn_pick_date.setEnabled(true);
            }
        });
    }

    private interface Callback_parkinglist {
        void onCallback_parkinglist(ArrayList<String> arraylist);
    }

    private void get_parking_list(final Callback_parkinglist callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> parking_list = new ArrayList<>();
                try {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Log.d("testt", "parking: " + snapshot.getKey());
                        parking_list.add(snapshot.getKey());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "parking_list DB get fail");
                }
                callback.onCallback_parkinglist(parking_list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
