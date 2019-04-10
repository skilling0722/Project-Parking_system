package com.example.parkingsystem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    public HashMap<String, Integer> cal_year_period(String start_date, String end_date) {
        String start_year = start_date.substring(0, 4);
        String end_year = end_date.substring(0, 4);

        HashMap<String, Integer> year_map = new HashMap<>();
        year_map.put("start_year", Integer.parseInt(start_year));
        year_map.put("end_year", Integer.parseInt(end_year));
        return year_map;
    }

    public void month_usage_analysis(final Callback_month_usage callback, String spot, Integer start_date, Integer end_date) {
        HashMap<String, Integer> year_map = cal_year_period(Integer.toString(start_date), Integer.toString(end_date));

        ArrayList<HashMap> reg_month_map_list = new ArrayList<>();

        int gap = Math.abs( year_map.get("start_year") - year_map.get("end_year") );
        if ( gap == 0 ) {
            //연도 같음
            reg_month_map_list.add(get_reg_for_month_analysis(year_map.get("start_year")));
        } else {
            if ( year_map.get("start_year") > year_map.get("end_year") ) {
                for ( int year = year_map.get("end_year"); year <= year_map.get("end_year") + gap; year++ ) {
//                    Log.d("testt", "start_year가 더 클때 year: "+ year);
                    reg_month_map_list.add(get_reg_for_month_analysis(year));
                }
            } else {
                for ( int year = year_map.get("start_year"); year <= year_map.get("start_year") + gap; year++ ) {
//                    Log.d("testt", "end_year가 더 클때 year: "+ year);
                    reg_month_map_list.add(get_reg_for_month_analysis(year));
                }
            }
        }
        final ArrayList<HashMap> final_reg_month_map_list = reg_month_map_list;

//        Log.d("testt", "final reg_month_map_list: "+ final_reg_month_map_list);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").child(spot).orderByChild("use").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, Integer> month_count = null;
                try {
                    month_count = init_month_count_map();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
//                        Log.d("testt", "extract data: " + data.getDate() + ", " + data.getTime() + ", "+data.isUse());
                        for ( int year_seq = 0; year_seq < final_reg_month_map_list.size(); year_seq++ ) {
                            for ( int month_seq = 1; month_seq <= 12; month_seq++ ) {
//                                Log.d("testt", "테스트값: " +(String) (final_reg_month_map_list.get(year_seq)).get(month_seq));
                                if ((""+data.getDate()).matches((String) (final_reg_month_map_list.get(year_seq)).get(month_seq))) {
                                    month_count.put(month_seq, month_count.get(month_seq)+1);
                                }
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

    //날짜 정렬, 옛날이 start가 될 수 있도록
    public Integer[] date_swap(Integer start_date, Integer end_date) {
        if ( start_date < end_date ) {
            ;
        } else if ( start_date > end_date ){
            int temp = start_date;
            start_date = end_date;
            end_date = temp;
        }
        return new Integer[]{start_date, end_date};
    }

    public void day_usage_analysis(final Callback_day_usage callback, String spot, Integer start_date, Integer end_date) {
        Integer[] date_arr = date_swap(start_date, end_date);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").child(spot).orderByChild("date").startAt(date_arr[0]).endAt(date_arr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, Integer> day_count = null;
                try {
                    day_count = init_day_count_map();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
                        if ( data.isUse() ) {
                            day_count.put(data.getTime(), day_count.get(data.getTime()) + 1);
                        }
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

    public void week_usage_analysis(final Callback_week_usage callback, String spot, Integer start_date, Integer end_date) {
        Integer[] date_arr = date_swap(start_date, end_date);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("analysis").child(spot).orderByChild("date").startAt(date_arr[0]).endAt(date_arr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, Integer> week_count = null;
                assistant assistant = new assistant();
                try {
                    week_count = init_week_count_map();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
//                        Log.d("testt", "extract data: " + data.getDate() + ", " + data.getTime() + ", "+data.isUse());
                        if ( data.isUse() ) {
                            int day_of_week = assistant.get_dayofweek(Integer.toString(data.getDate()));
//                          Log.d("testt", "변환 요일   "+day_of_week);
                            week_count.put(day_of_week, week_count.get(day_of_week) + 1);
                        }
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
