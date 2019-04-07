package com.example.parkingsystem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;

public class parking_check extends AppCompatActivity {
    private listview_adapter adapter;

    @BindView(R.id.parking_check_listview) ListView parking_check_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_check);

        create_listview();

        check_parking_spots(new Callback_spots() {
            @Override
            public void onCallback_spots(final ArrayList arraylist) {
//                Log.d("testt", "가져온 리스트   " + arraylist);
                try {
                    Log.d("testt", "add_view start");
                    for(int i = 0; i < arraylist.size(); i++ ) {
                        final int final_i = i;

                        check_remaining_spots(new Callback_remaining() {
                            @Override
                            public void onCallback_remaining(int remaining_count, int all_count) {
                                add_view(getResources().getDrawable(R.drawable.ic_launcher), (String) arraylist.get(final_i), remaining_count + " / " + all_count);
                                adapter.notifyDataSetChanged();
                            }

                        }, (String) arraylist.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "add_view fail");
                }
            }
        });
    }

    public void create_listview() {
        ListView listview;
        adapter = new listview_adapter();

        listview = (ListView) findViewById(R.id.parking_check_listview);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listview_item item = (listview_item) parent.getItemAtPosition(position);
                String spot = item.getSpot_title();
                String remain = item.getRemain_title();
                Drawable iconDrawable = item.getIcon();

                Intent intent = new Intent(getApplicationContext(), spot_check.class);
                intent.putExtra("spot", spot);
                intent.putExtra("space", remain);

                startActivity(intent);
            }
        });
    }

    public void add_view(Drawable Icon, String title, String remaining_spot) {
        //주차장별로 이미지 바꿀라면 파라미터로 받자
        adapter.addItem(Icon, title, remaining_spot);
    }

    public interface Callback_spots {
        void onCallback_spots(ArrayList arraylist);
    }

    public interface Callback_remaining {
        void onCallback_remaining(int remaining_count, int all_count);
    }

    public void check_parking_spots(final Callback_spots callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList parking_list = new ArrayList();
                try {
                    Log.d("testt", "check_parking_spots start");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Log.d("testt", snapshot.getKey() + "      " + String.valueOf(snapshot.getValue()));
                        parking_list.add(snapshot.getKey());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "check_parking_spots fail");
                }

                //주차 공간 확인 리스트 만들때 쓰자
//                Log.d("testt", "parking_list size: " + parking_list.size() + " /// value " + parking_list);
                callback.onCallback_spots(parking_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void check_remaining_spots(final Callback_remaining callback, String spot) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").child(spot).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int remaining_count = 0;
                int all_count = 0;
                try {
//                    Log.d("testt", "check_remaining_spots start");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Data_for_check data = snapshot.getValue(Data_for_check.class);
//                        data.isUse() 값으로 주차 가능유무를, snapshot.getKey() 로 주차자리를 정해주자.
//                        Log.d("testt", "주차 가능 유무: " + data.isUse() + ", key(자리): " + snapshot.getKey());

                        if (data.isUse()) {
                            remaining_count ++;
                        }
                        all_count ++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "check_remaining_spots fail");
                }
                //remaining_count랑 all_count로 주차 공간 확인 화면에 textview 갱신시켜주자
//                Log.d("testt", "remaining spot: " + remaining_count + "/" + all_count);
                callback.onCallback_remaining(remaining_count, all_count);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
