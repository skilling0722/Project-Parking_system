package com.example.parkingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class newtontalk_test extends AppCompatActivity implements TextToSpeechListener {

    @BindView(R.id.tv_result)
    TextView tv_result;
    @BindView(R.id.iv_mic)
    ImageView iv_mic;
    @BindView(R.id.bt_search)
    Button bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtontalk_test);
        ButterKnife.bind(this);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());
    }

    @OnClick(R.id.bt_search)
    void bt_talkbuttonClick2() {
        Log.d("testt", "음성 합성 시작");

        TextToSpeechClient client = new TextToSpeechClient.Builder().
                setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1).
                setSpeechSpeed(1.0).
                setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_DIALOG_BRIGHT).
                setListener(this).
                build();

        Toast.makeText(this, "음성합성을 시작합니다.", Toast.LENGTH_SHORT).show();

        String str = "안녕하세요. 저는 카카오 음성 합성 API입니다.";
        str = "55호관 주차장이에요. 현재 18 자리 중 3 자리 남아있어요.";
        try {
            client.setSpeechText(str);
            client.play();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "음성 합성 실패");
        }

    }

    public void onDestroy() {
        super.onDestroy();
        SpeechRecognizerManager.getInstance().finalizeLibrary();
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onFinished() {
        Log.d("testt", "음성 합성 종료");
    }

    @Override
    public void onError(int code, String message) {
        Log.d("testt", "onError: " + message);
    }
}
