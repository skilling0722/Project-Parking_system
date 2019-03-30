package com.example.parkingsystem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Firebase_assist {
    private DatabaseReference mDatabase;
    static  int total_count;
    public Firebase_assist() {
        mDatabase = FirebaseDatabase.getInstance().getReference("/parking_spots");
    }


    public void write_db_day_count(String parking_spots, String parking_spot, String day, String time,String num) {
        Data_form parking_info = new Data_form(num);
        mDatabase.child(parking_spot).child(day).child(time).setValue(parking_info);
    }

    public void write_db_allcount(String parking_spots, String parking_spot, String day){
        mDatabase.child(parking_spots).child(parking_spot).child(day);
    }

    public void get_allcount_from_spot(String parking_spots){
        total_count = 0;
        for (int i = 0; i < 24; i++){
            get_from_database(Integer.toString(i));
        }
        Log.d("testt", "total_count: " + total_count);
    }

    public void get_from_database(String time) {
        mDatabase.child("b").child("20190331").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Log.d("testt", String.valueOf(snapshot.getValue()));
                        String count_val = String.valueOf(snapshot.getValue());
                        total_count += Integer.parseInt(count_val);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "fail");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
