package com.example.parkingsystem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Data_analysis_assistant {

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

    public HashMap<Integer, Integer> init_week_count_map() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for ( int i = 1; i <= 7; i ++) {
            map.put(i, 0);
        }
        return map;
    }

    public void month_usage_analysis(final Callback_month_usage callback, int year) {
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
//                        Log.d("testt", "extract data: " + data.getDate() + ", " + data.getTime() + ", "+data.isUse());

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

//                for ( int i = 1; i <= 12; i++ ) {
//                    Log.d("testt", "월: " +  i + ", month_count " +month_count.get(i));
//                }
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

    public void day_usage_analysis(final Callback_day_usage callback) {
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

//                for ( int i = 0; i <= 23; i++ ) {
//                    Log.d("testt", "시간: " +  i + ", day_count " + day_count.get(i));
//                }
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

    public void week_usage_analysis(final Callback_week_usage callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").child("a").orderByChild("use").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, Integer> week_count = null;
                assistant assistant = new assistant();
                try {
                    week_count = init_week_count_map();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
//                        Log.d("testt", "extract data: " + data.getDate() + ", " + data.getTime() + ", "+data.isUse());
                        int day_of_week = assistant.get_dayofweek(Integer.toString(data.getDate()));
//                        Log.d("testt", "변환 요일   "+day_of_week);
                        week_count.put(day_of_week, week_count.get(day_of_week) + 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "week_usage_analysis get DB fail");
                }
                callback.onCallback_week_usage(week_count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface Callback_week_usage {
        void onCallback_week_usage(HashMap<Integer, Integer> map);
    }


}
