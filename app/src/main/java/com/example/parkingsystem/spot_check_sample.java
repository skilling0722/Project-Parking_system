package com.example.parkingsystem;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

import org.w3c.dom.Text;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
주차장별 공간 확인 클래스 for 55호관
 */
public class spot_check_sample extends AppCompatActivity implements TextToSpeechListener {
    @BindView(R.id.spot_check_name) TextView spot_check_name;
    @BindView(R.id.realmap_button) Button realmap_button;
    @BindView(R.id.parkinglot1) TextView parkinglot1;
    @BindView(R.id.parkinglot3) TextView parkinglot3;
    @BindView(R.id.parkinglot4) TextView parkinglot4;
    @BindView(R.id.parkinglot7) TextView parkinglot7;
    @BindView(R.id.parkinglot8) TextView parkinglot8;
    @BindView(R.id.parkinglot10) TextView parkinglot10;
    @BindView(R.id.parkinglot13) TextView parkinglot13;

    private SharedPreferences prefs;
    private HashMap<String, Object> voice_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config_Activity.setBackground(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_check_sample);
        ButterKnife.bind(this);

        init_voicelist();
        final HashMap<String, TextView> parkinglot_map = init_parkinglot();

        TextToSpeechManager.getInstance().initializeLibrary(this);

        String spot = "";
        try {
            Intent intent = getIntent();
            spot = intent.getStringExtra("spot");
            spot_check_name.setText(spot + " 주차장");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "intent getString fail");
        }

        final String spot_param = spot;

        spot_check.read_spots(new spot_check.Callback_read_spots() {
            @Override
            public void onCallback_read_spots(HashMap<String, Boolean> spot_info) {
                try {
                    if ( spot_info.size() == 0 ) {
                        Log.d("testt", "주차장 정보 없음 에러");
                        return;
                    }

                    int total_lot = spot_info.size();
                    int use_lot = 0;

                    for(String key : spot_info.keySet()){
                        TextView view = parkinglot_map.get(key);
                        Boolean isuse = spot_info.get(key);

                        if ( isuse ) {
                            use_lot++;
                        }

                        update_ui(view, isuse);
                    }

                    if ( prefs.getBoolean("voice_notify", true) ){
                        call_tts(spot_param, use_lot + " / " + total_lot);
                    } else {
                        Log.d("testt", "음성 합성 비활성화 상태");
                        Toast.makeText(getApplicationContext(), "음성 알림이 비활성화 상태입니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "recyclerview fail");
                }
            }
        }, spot);
    }

    /*
    주차면 UI 업데이트
     */
    private void update_ui(TextView view, Boolean isuse) {
        if ( isuse ) {
            /* 사용중             */
            view.setBackgroundColor(Color.BLUE);
        } else {
            /* 비어있음             */
            view.setBackgroundColor(Color.RED);
        }
    }

    /*
    주차면 init
     */
    private HashMap<String, TextView> init_parkinglot() {
        HashMap<String, TextView> parkinglot_map = new HashMap<>();
        parkinglot_map.put("1", parkinglot1);
        parkinglot_map.put("2", parkinglot3);
        parkinglot_map.put("3", parkinglot4);
        parkinglot_map.put("4", parkinglot7);
        parkinglot_map.put("5", parkinglot8);
        parkinglot_map.put("6", parkinglot10);
        parkinglot_map.put("7", parkinglot13);
        return parkinglot_map;
    }

    /*
    음성 합성 타입 init
    맵(스트링, 음성 합성 타입)
     */
    private void init_voicelist() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        voice_map = new HashMap<>();
        voice_map.put("남성 차분한 낭독체", TextToSpeechClient.VOICE_MAN_READ_CALM);
        voice_map.put("남성 밝은 대화체", TextToSpeechClient.VOICE_MAN_DIALOG_BRIGHT);
        voice_map.put("여성 차분한 낭독체", TextToSpeechClient.VOICE_WOMAN_READ_CALM );
        voice_map.put("여성 밝은 대화체", TextToSpeechClient.VOICE_WOMAN_DIALOG_BRIGHT);
    }

    /*
    음성 합성시 음성 인식 비프음 스킵을 위한 istts값 설정
     */
    public void write_istts(Boolean istts) {
        SharedPreferences shared_tts = getSharedPreferences("istts", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_tts.edit();
        editor.putBoolean("usage", istts);
        editor.commit();
    }

    /*
    음성 합성 호출
     */
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
            Log.d("testt", text);
        }

        try {
            client.setSpeechText(text);
            client.play();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "음성 합성 실패");
        }
    }

    /*
    버튼 클릭시 실제 주차장 이미지 다이얼로그 띄우기
     */
    @OnClick(R.id.realmap_button)
    void realmap_view() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.realmap_dialog);
        dialog.show();
    }

    /*
    음성 합성 종료시 호출
     */
    @Override
    public void onFinished() {
        Log.d("testt", "음성 합성 종료");
//        Log.d("testt", "istts false 쓰기 ");
        write_istts(false);
        //        SpeechRecognizerManager.getInstance().finalizeLibrary();
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    /*
    음성 합성 에러시 호출
     */
    @Override
    public void onError(int code, String message) {
        Log.d("testt", "onError: " + message);
    }

    /*
    spot_check_sample 액티비티 종료시 호출
     */
    public void onDestroy() {
        super.onDestroy();
        write_istts(false); //뒤로가기시 음성 합성 istts 값 false로
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

}
