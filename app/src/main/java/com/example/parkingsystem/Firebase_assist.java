package com.example.parkingsystem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class Firebase_assist {
    private DatabaseReference mDatabase;

    public Firebase_assist() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void writeDB_for_analysis(String analysis, String parking_spot, int date, int time, boolean use) {
        Data_for_analysis parking_info = new Data_for_analysis(date, time, use);
        mDatabase.child(analysis).child(parking_spot).push().setValue(parking_info);
    }

    public void getDB_for_analysis(String analysis) {
        mDatabase.child(analysis).child("a").orderByChild("use").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_analysis data = snapshot.getValue(Data_for_analysis.class);
                        Log.d("testt", "extract data: " + data.getDate() + ", " + data.getTime() + ", "+data.isUse());
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

    public void writeDB_for_check(String check, String parking_spots, String spot, boolean use) {
        Map<String, Object> spot_box = new HashMap<>();
        Map<String, Boolean> temp = null;

        Data_for_check parking_info = new Data_for_check(use);
        temp = parking_info.convert_map();

        spot_box.put(spot, temp);
        mDatabase.child(check).child(parking_spots).updateChildren(spot_box);
    }

    public void getDB_for_check(String check) {
        mDatabase.child(check).child("a").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_check data = snapshot.getValue(Data_for_check.class);
                        Log.d("testt", "for check, extract data: " + snapshot.getKey() + data.isUse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "for check, fail");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
