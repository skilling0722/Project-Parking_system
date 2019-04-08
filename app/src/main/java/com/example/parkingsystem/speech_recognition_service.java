package com.example.parkingsystem;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import java.util.ArrayList;

public class speech_recognition_service extends Service implements SpeechRecognizeListener {
    private ServiceCallbacks serviceCallbacks;
    private static SpeechRecognizerClient client;
    private static Boolean isuse;

    private SoundPool sound_pool;
    private int beep_sound;

    IBinder mBinder =  new speech_recognition_binder();

    class speech_recognition_binder extends Binder {
        speech_recognition_service getService() {
            return speech_recognition_service.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("testt", "onBind");
        return mBinder;
    }

    public void onCreate() {
        Log.d("testt", "onCreate");
        super.onCreate();

        create_soundpool();
        beep_sound = sound_pool.load(getApplicationContext(), R.raw.beep, 1);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        isuse = true;

        call_speech_recognition();
    }


    public void create_soundpool() {
        AudioAttributes audioAttributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sound_pool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        }
        else {
            sound_pool = new SoundPool(8, AudioManager.STREAM_NOTIFICATION, 0);
        }
    }

    public void call_speech_recognition() {
        Log.d("testt", "call_speech_recognition");
        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;

        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder()
                .setServiceType(serviceType)
                .setGlobalTimeOut(6);
        client = builder.build();
        client.setSpeechRecognizeListener(this);
        client.startRecording(false);    // 음성 인식 시작시 재생되고 있던 소리 mute, true면 mute

//        Toast.makeText(this, "음성 인식 시작", Toast.LENGTH_SHORT).show();
    }

    public static void cancel_speech_recognition() {
        Log.d("testt", "cancel_speech_recognition");
        client.cancelRecording();
        isuse = false;
    }


    public int onStartCommand(Intent intent, int flags, int startid) {
        Log.d("testt", "onStartCommand");
        return super.onStartCommand(intent, flags, startid);
    }

    @Override
    public void onDestroy() {
        Log.d("testt", "onDestroy");
        SpeechRecognizerManager.getInstance().finalizeLibrary();
        isuse = false;
        super.onDestroy();
    }

    public interface Callback_restart {
        void onCallback_restart();
    }

    //기기, 오디오 준비 끝나면 호출
    @Override
    public void onReady() {
        Log.d("testt", "음성 인식 준비 완료");
//        cancel_speech_recognition();

        SharedPreferences shared_tts = getSharedPreferences("istts", MODE_PRIVATE);
        Boolean istts = shared_tts.getBoolean("usage", true);
        Log.d("testt", "istts: " + istts);


        if (!istts) {
            Log.d("testt", "beep sound");
            sound_pool.play(beep_sound, 0.1f, 0.1f, 0, 0, 2.0f);
        } else {
            Log.d("testt", "음성 합성 실행중, 비프음 스킵");
        }


        //리소스 식별 번호, 좌측 볼륨, 우측 볼륨, 우선순위, 반복, 재생속도
    }
    //음성 인식 시작하면 호출
    @Override
    public void onBeginningOfSpeech() {
        Log.d("testt", "음성 입력 시작");
    }

    //음성 인식 끝나면 호출
    @Override
    public void onEndOfSpeech() {
        Log.d("testt", "입력 종료");
    }
//    //에러나면 호출
//    @Override
//    public void onError(int errorCode, String errorMsg) {
//        Log.d("testt", "onError: " + errorMsg);
//    }

    //에러나면 호출
    @Override
    public void onError(int errorCode, String errorMsg) {
        Log.d("testt", "onError: " + errorMsg);

        if (isuse) {
            restart(new Callback_restart() {
                @Override
                public void onCallback_restart() {
                    try {
                        Log.d("testt", "재시작한다3333");
                        call_speech_recognition();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testt", "재시작 안대여: " + e);
                    }
                }
            });
        }
    }

    public void restart(final Callback_restart callback) {
        callback.onCallback_restart();
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

        final ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);

        Log.d("testt", "Result: " + texts);

        if(serviceCallbacks != null) {
            serviceCallbacks.do_something(texts.get(0));
        }
    }

    @Override
    public void onAudioLevel(float audioLevel) {
//        Log.d("testt", "audioLevel: " + audioLevel);
    }
    //API 종료시 호출
    @Override
    public void onFinished() {
        Log.d("testt", "onFinished: ");

        restart(new Callback_restart() {
            @Override
            public void onCallback_restart() {
//                Log.d("testt", "음성 인식 재시작");
                try {
                    call_speech_recognition();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "재시작 안대여: " + e);
                }
            }
        });
    }

    public interface ServiceCallbacks {
        void do_something(String result);
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

}
