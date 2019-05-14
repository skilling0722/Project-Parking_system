package com.example.parkingsystem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
주차 공간 확인 리스트형 클래스
 */
public class parking_check extends BaseActivity {
    private listview_adapter adapter;
    private int[] images;
    public static HashMap<String, String> for_speech_spots;
    Random random;
    @BindView(R.id.parking_check_listview) ListView parking_check_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config_Activity.setBackground(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_check);
        ButterKnife.bind(this);
        show_loading();

        init_drawable();
        create_listview();
        check_parking_spots(new Callback_spots() {
            @Override
            public void onCallback_spots(final ArrayList arraylist) {
//                Log.d("testt", "가져온 리스트   " + arraylist);
                for_speech_spots = new HashMap<>();

                try {
                    Log.d("testt", "add_view start");
                    for(int i = 0; i < arraylist.size(); i++ ) {
                        final int final_i = i;

                        check_remaining_spots(new Callback_remaining() {
                            @Override
                            public void onCallback_remaining(int remaining_count, int all_count) {
                                add_view(getResources().getDrawable(random_drawable()), (String) arraylist.get(final_i), remaining_count + " / " + all_count);
                                for_speech_spots.put((String) arraylist.get(final_i), remaining_count + " / " + all_count);
                                adapter.notifyDataSetChanged();
                                hide_loading();
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

    public static HashMap<String, String> getFor_speech_spots() {
        return for_speech_spots;
    }

    private void init_drawable() {
        images = new int[] {R.drawable.parking_check1, R.drawable.parking_check2, R.drawable.parking_check3, R.drawable.parking_check4,
                R.drawable.parking_check5, R.drawable.parking_check6, R.drawable.parking_check7, R.drawable.parking_check8, R.drawable.parking_check9,
                R.drawable.parking_check10, R.drawable.parking_check11, R.drawable.parking_check12, R.drawable.parking_check13};
        random = new Random();
    }

    private int random_drawable() {
        return images[random.nextInt(images.length)];
    }


    public void create_listview() {
        adapter = new listview_adapter();
        parking_check_listview.setAdapter(adapter);

        parking_check_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listview_item item = (listview_item) parent.getItemAtPosition(position);

                String spot_name = item.getSpot_title();
                String spare = item.getRemain_title();
//                Drawable iconDrawable = item.getIcon();

                if ( spot_name.equals("55호관") ) {
                    Intent intent = new Intent(getApplicationContext(), spot_check_sample.class);
                    intent.putExtra("spot", spot_name);
                    intent.putExtra("space", spare);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), spot_check.class);
                    intent.putExtra("spot", spot_name);
                    intent.putExtra("space", spare);
                    startActivity(intent);
                }
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

    public static void check_parking_spots(final Callback_spots callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList parking_list = new ArrayList();
                try {
                    Log.d("testt", "check_parking_spots start");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Log.d("testt", "test: "+snapshot);
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



    public static void check_remaining_spots(final Callback_remaining callback, String spot) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").child(spot).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int remaining_count = 0;
                int all_count = 0;
                try {
//                    Log.d("testt", "check_remaining_spots start");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (!snapshot.getKey().equals("position")){
//                            Log.d("testt", "자리 확인: "+snapshot);
                            Data_for_check data = snapshot.getValue(Data_for_check.class);
//                           data.isUse() 값으로 주차 가능유무를, snapshot.getKey() 로 주차자리를 정해주자.
//                           Log.d("testt", "주차 가능 유무: " + data.isUse() + ", key(자리): " + snapshot.getKey());

                            if (data.isUse()) {
                                remaining_count ++;
                            }
                            all_count ++;
                        }
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
