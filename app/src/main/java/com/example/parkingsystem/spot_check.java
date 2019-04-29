package com.example.parkingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class spot_check extends AppCompatActivity implements TextToSpeechListener {
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    @BindView(R.id.spot_check_name) TextView spot_check_name;
    private SharedPreferences prefs;
    private HashMap<String, Object> voice_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config_Activity.setBackground(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_check);
        ButterKnife.bind(this);

        init_voicelist();

        TextToSpeechManager.getInstance().initializeLibrary(this);
//        Log.d("testt", "spot_check activity start");
        String spot = "";
        String space = "";
        try {
            Intent intent = getIntent();
            spot = intent.getStringExtra("spot");
            space = intent.getStringExtra("space");
//            Log.d("testt", "spot_check: " +spot + "    " +space);
            spot_check_name.setText(spot + " 주차장");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "intent getString fail");
        }

        if ( prefs.getBoolean("voice_notify", true) ) {
            call_tts(spot, space);
        } else {
            Log.d("testt", "음성 합성 비활성화 상태");
        }

        read_spots(new Callback_read_spots() {
            @Override
            public void onCallback_read_spots(HashMap<String, Boolean> spot_info) {
                try {
//                    Log.d("testt", "recyclerview start");
                    create_recyclerview(spot_info);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "recyclerview fail");
                }
            }
        }, spot);
    }

    private void init_voicelist() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        voice_map = new HashMap<>();
        voice_map.put("남성 차분한 낭독체", TextToSpeechClient.VOICE_MAN_READ_CALM);
        voice_map.put("남성 밝은 대화체", TextToSpeechClient.VOICE_MAN_DIALOG_BRIGHT);
        voice_map.put("여성 차분한 낭독체", TextToSpeechClient.VOICE_WOMAN_READ_CALM );
        voice_map.put("여성 밝은 대화체", TextToSpeechClient.VOICE_WOMAN_DIALOG_BRIGHT);
    }

    private void create_recyclerview(HashMap<String, Boolean> spot_info) {
        ArrayList<recyclerview_item> items = new ArrayList<>();

        for ( Object e : spot_info.keySet() ) {
//            Log.d("testt", e + ": " + spot_info.get(e));
            items.add(new recyclerview_item((String) e, spot_info.get(e)));
        }

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview_adapter adapter = new recyclerview_adapter(items, R.layout.recyclerview_item, this);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(manager);
    }

    public void write_istts(Boolean istts) {
        SharedPreferences shared_tts = getSharedPreferences("istts", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_tts.edit();
        editor.putBoolean("usage", istts);
        editor.commit();
    }

    public void call_tts(String spot, String space) {
        Log.d("testt", "음성 합성 시작");
//        Log.d("testt", "istts true 쓰기 ");
        write_istts(true);

        String using = space.split(" / ")[0];
        String all = space.split(" / ")[1];
        String remain = Integer.toString(Integer.parseInt(all) - Integer.parseInt(using));

        TextToSpeechClient client = new TextToSpeechClient.Builder().
                setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1).
                setSpeechSpeed(1.0).
                setSpeechVoice((String) voice_map.get(prefs.getString("keyword_voice_list", "여성 밝은 대화체"))).
                setListener(this).
                build();

        Toast.makeText(this, "음성합성을 시작합니다.", Toast.LENGTH_SHORT).show();

        String text;
        if ( remain.equals("0") ) {
            /* 자리 없을 때 */
            text = spot + " 주차장은 자리가 없어요.";
        } else {
            /* 자리 남아 있을 때 */
            text = spot + " 주차장이에요. 현재 " + all + " 자리 중 " + remain + " 자리 남아있어요.";
        }

        try {
            client.setSpeechText(text);
            client.play();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "음성 합성 실패");
        }
    }

    @Override
    public void onFinished() {
        Log.d("testt", "음성 합성 종료");
//        Log.d("testt", "istts false 쓰기 ");
        write_istts(false);
        //        SpeechRecognizerManager.getInstance().finalizeLibrary();
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onError(int code, String message) {
        Log.d("testt", "onError: " + message);
    }

    public void onDestroy() {
        super.onDestroy();
        write_istts(false);
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    public interface  Callback_read_spots {
        void onCallback_read_spots(HashMap<String, Boolean> spot_info);
    }

    public void read_spots(final Callback_read_spots callback, String spot) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").child(spot).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Boolean> spot_info = new HashMap<>();
                try {
//                    Log.d("testt", "spot_check start");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (!snapshot.getKey().equals("position") ) {
                            Data_for_check data = snapshot.getValue(Data_for_check.class);
//                        Log.d("testt", "주차 가능 유무: " + data.isUse() + ", key(자리): " + snapshot.getKey());
                            spot_info.put(snapshot.getKey(), data.isUse());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "spot_check fail");
                }
                callback.onCallback_read_spots(spot_info);
//                Log.d("testt", "spot_check activity stop");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
