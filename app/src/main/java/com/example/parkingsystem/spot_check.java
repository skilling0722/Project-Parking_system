package com.example.parkingsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class spot_check extends AppCompatActivity {
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    @BindView(R.id.spot_check_name) TextView spot_check_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_check);
        ButterKnife.bind(this);
        Log.d("testt", "spot_check activity start");
        String spot = "";


        try {
            Intent intent = getIntent();
            spot = intent.getStringExtra("spot");
            spot_check_name.setText(spot + " 주차장");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "intent getString fail");
        }



        read_spots(new Callback_read_spots() {
            @Override
            public void onCallback_read_spots(HashMap<String, Boolean> spot_info) {
                try {
                    Log.d("testt", "recyclerview start");
                    create_recyclerview(spot_info);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "recyclerview fail");
                }
            }
        }, spot);
    }

    private void create_recyclerview(HashMap<String, Boolean> spot_info) {
        Set keyset = spot_info.keySet();

        ArrayList<recyclerview_item> items = new ArrayList<>();

        for ( Object e : keyset ) {
            Log.d("testt", e + ": " + spot_info.get(e));
            items.add(new recyclerview_item((String) e, spot_info.get(e)));
        }

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_adapter adapter = new recyclerview_adapter(items, R.layout.recyclerview_item, this);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(manager);
    }

    public interface  Callback_read_spots {
        void onCallback_read_spots(HashMap<String, Boolean> spot_info);
    }

    public void read_spots(final Callback_read_spots callback, String spot) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").child(spot).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<Data_for_check> spot_usage = new ArrayList<>();
                HashMap<String, Boolean> spot_info = new HashMap<>();
                try {
                    Log.d("testt", "spot_check start");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_check data = snapshot.getValue(Data_for_check.class);
                        Log.d("testt", "주차 가능 유무: " + data.isUse() + ", key(자리): " + snapshot.getKey());
                        spot_info.put(snapshot.getKey(), data.isUse());
//                        spot_usage.add(data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "spot_check fail");
                }
                callback.onCallback_read_spots(spot_info);
                Log.d("testt", "spot_check activity stop");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
