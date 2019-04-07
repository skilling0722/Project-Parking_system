package com.example.parkingsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class newton_test extends AppCompatActivity implements SpeechRecognizeListener{

    private static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 0;
    private SpeechRecognizerClient client;

    @BindView(R.id.tv_result) TextView tv_result;
    @BindView(R.id.iv_mic) ImageView iv_mic;
    @BindView(R.id.bt_search) Button bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_test);
        ButterKnife.bind(this);

        try {
            Log.d("testt", "권한 획득 시작");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE);
                } else {
                    // 유저가 거부하면서 다시 묻지 않기를 클릭.. 권한이 없다고 유저에게 직접 알림.
                }
            } else {
//                startUsingSpeechSDK();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "권한 획득 오류");
        }

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        // 클라이언트 생성
//        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB);
//        .setUserDictionary(userdict);  // optional


    }

    public void onDestroy() {
        super.onDestroy();

        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    @OnClick(R.id.bt_search)
    void bt_recog_buttonClick2() {
        Log.d("testt", "음성 인식 시작");

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;
        Log.i("testt", "ServiceType : " + serviceType);

        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(serviceType);
        client = builder.build();
        client.setSpeechRecognizeListener(this);
        client.startRecording(true);    // 음성 인식 시작시 재생되고 있던 소리 mute, true면 mute

        Toast.makeText(this, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();

    }

    //기기, 오디오 준비 끝나면 호출
    @Override
    public void onReady() {
        Log.d("testt", "모든 준비가 완료 되었습니다.");
    }
    //음성 인식 시작하면 호출
    @Override
    public void onBeginningOfSpeech() {
        Log.d("testt", "말하기 시작 했습니다.");
    }

    //음성 인식 끝나면 호출
    @Override
    public void onEndOfSpeech() {
        Log.d("testt", "입력 종료");
    }
    //에러나면 호출
    @Override
    public void onError(int errorCode, String errorMsg) {
        Log.d("testt", "onError: " + errorMsg);
    }
    //음성 인식 도중 결과물 반환, 지속적 호출
    @Override
    public void onPartialResult(String partialResult) {
        Log.d("testt", "onPartialResult: " + partialResult);
    }
    //음성 인식 종료후 결과 반환시 호출
    @Override
    public void onResults(Bundle results) {
        Log.d("testt", "onResults start");
//        final StringBuilder builder = new StringBuilder();

        final ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
//        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        Log.d("testt", "Result: " + texts);

//        for (int i = 0; i < texts.size(); i++){
//            builder.append(texts.get(i));
//            builder.append(" (");
//            builder.append(confs.get(i).intValue());
//            builder.append(")\n");
//        }

        //모든 콜백함수들은 백그라운드에서 돌고 있기 때문에 메인 UI를 변경할려면 runOnUiThread를 사용해야 한다.
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(activity.isFinishing()) return;

                tv_result.setText(texts.get(0));

                if (texts.get(0).equals("주차 공간 분석")) {
                    Intent intent = new Intent(activity, parking_check.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onAudioLevel(float audioLevel) {
//        Log.d("testt", "audioLevel: " + audioLevel);

    }
    //API 종료시 호출
    @Override
    public void onFinished() {
        Log.d("testt", "onFinished: ");
    }


}
