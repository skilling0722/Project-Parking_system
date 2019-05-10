package com.example.parkingsystem;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import java.util.HashMap;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements speech_recognition_service.ServiceCallbacks {
    @BindView(R.id.button1) Button btn1;
    @BindView(R.id.button2) Button btn2;
    @BindView(R.id.button3) Button btn3;
    @BindView(R.id.firebase_test) Button firebase_test_btn;

    @BindView(R.id.test_btn1) Button test_btn11;
    @BindView(R.id.test_btn2) Button test_btn22;
    @BindView(R.id.test_btn3) Button test_btn33;

    private static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 0;
    private speech_recognition_service srs_service;
    private boolean isService;
    private HashMap<String, String> spots_map = new HashMap<>();    //parking_check 액티비티에서 가져온 hashmap 저장할 곳
    private ServiceConnection conn;
    private SharedPreferences prefs;

//    ServiceConnection conn = new ServiceConnection() {
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            speech_recognition_service.speech_recognition_binder srs_binder = (speech_recognition_service.speech_recognition_binder) service;
//            srs_service = srs_binder.getService();
//            isService = true;
//            srs_service.setCallbacks(MainActivity.this);
//        }
//        public void onServiceDisconnected(ComponentName name) {
//            srs_service = null;
//            isService = false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config_Activity.setBackground(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
            Log.d("testt", "권한 획득 시작");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE);
                } else {
                    // 유저 거부
                }
            } else {
                //ㅇ
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "권한 획득 오류");
        }

        create_service();

        /* 설정 - 음성 알림 활성/비활성에 따른 음성 인식 서비스 onoff*/
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if ( prefs.getBoolean("voice_command", true)) {
            start_service();
        } else {
            Log.d("testt", "음성 인식 비활성화 상태");
            /*
            stt를 초기화해주지 않으면 tts가 안되는 문제가 있음. - 카카오 api 문제
            임시 해결방안으로 stt 서비스 사용안할시 stt 초기화해주자.
             */
            SpeechRecognizerManager.getInstance().initializeLibrary(this);
        }

        assistant assistant = new assistant();
        assistant.setIstts(false);

        /* 어플리케이션 강제종료, 예상치못한 종료를 대비하여 istts 초기값 false 초기화 */
        SharedPreferences shared_tts = getSharedPreferences("istts", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_tts.edit();
        editor.putBoolean("usage", false);
        editor.commit();


//        mContext = getApplicationContext();
//        String key = getKeyHash(mContext);
//        Log.d("testt", "Key:" + key);
    }
    @Override
    public void onStart() {
        super.onStart();
//        show_loading();
//        hide_loading();
    }

    /*
    서비스 구현 테스트 버튼
     */
    @OnClick(R.id.test_btn1)
    void  button122Click() {
        start_service();
    }

    @OnClick(R.id.test_btn2)
    void  button13131Click() {
//        Intent intent = new Intent(this, map_test2.class);
//        startActivity(intent);
//        use_service();
    }

    @OnClick(R.id.test_btn3)
    void  button1311131Click() {
        stop_service();
    }

    /*
        실 사용 버튼
     */
    @OnClick(R.id.button1)
    void  Parking_check() {
        /* 주차장 확인 */
        if ( prefs.getBoolean("check_style", true) ) {
            /* 지도 형식 */
            Intent intent = new Intent(this, parking_check_map.class);
            startActivity(intent);
        } else {
            /* 리스트 형식 */
            Intent intent = new Intent(this, parking_check.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.button2)
    void Data_analysis() {
        /* 주차 정보 분석 */
        Intent intent = new Intent(this, menu_analysis_Activity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button3)
    void config() {
        /* 설정화면 */
        Intent intent = new Intent(this, Config_Activity.class);
        startActivity(intent);

    }

    @OnClick(R.id.firebase_test)
    void test_db() {
        /* 샘플 데이터 생성 */
        Intent intent = new Intent(this, DB_test.class);
        startActivity(intent);
    }

    /*
    음성 인식 서비스 초기화
     */
    private void create_service() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                speech_recognition_service.speech_recognition_binder srs_binder = (speech_recognition_service.speech_recognition_binder) service;
                srs_service = srs_binder.getService();
                isService = true;
                srs_service.setCallbacks(MainActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                srs_service = null;
                isService = false;
            }
        };
    }

    /*
    음성 인식 서비스 시작
     */
    private void start_service() {
        try {
            Intent intent = new Intent(MainActivity.this, speech_recognition_service.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
            Log.d("testt", "start_service");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "start_service error: " + e);
        }
    }

    /*
    음성 인식 서비스 중단
     */
    private void stop_service() {
        if (isService) {
            unbindService(conn);
            isService = false;
            Log.d("testt", "stop_service");
        }
        else {
            Log.d("testt", "중지할 서비스 없음");
        }
    }

//    private void use_service() {
//        SharedPreferences shared_tts = getSharedPreferences("istts", MODE_PRIVATE);
//        Boolean istts = shared_tts.getBoolean("usage", true);
//        Log.d("testt", "istts: " + istts);
//
//        if( !isService ) {
//            Log.d("testt", "음성 인식 서비스가 실행되고있지않아.");
//            return;
//        }
//        int num = srs_service.getRan();
//        Log.d("testt", "srs_service로 부터 받아온 값: " + num);
//    }

    /*
    음성 인식 결과로 액티비티 전환, 종료 등 실행
     */
    @Override
    public void do_something(String result) {
        Log.d("testt", "음성 인식 결과: " + result);
        spots_map = parking_check.getFor_speech_spots();
        if( result.equals("주차장") ) {
            Parking_check();
//            spots_map = parking_check.getFor_speech_spots();
        }
        else if ( result.equals("분석") ) {
            Data_analysis();
        }
        else if ( result.equals("설정") ) {
            config();
        }
        else if ( result.equals("db") ) {
            test_db();
        }
        else if ( result.equals("뒤로") ) {
            Process.killProcess(Process.myPid());
        }
        else if ( result.equals("종료") ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
            System.runFinalization();
            System.exit(0);
        }

        /*
        음성 인식으로 주차장 확인
         */
        else if ( spots_map.containsKey(result) ) {
            Intent intent = new Intent(getApplicationContext(), spot_check.class);
            intent.putExtra("spot", result);
            intent.putExtra("space", spots_map.get(result));
            startActivity(intent);
        }
        else {
            Log.d("testt", "음성 인식에 해당되는 것 없음");
        }
    }
    /* onCreate()후에 재실행될때마다 실행된다. */
    @Override
    public void onRestart(){
        super.onRestart();
        // put your code here...
        System.out.println("hello world");
        this.recreate();
    }

//    key 얻기
//    public static String getKeyHash(final Context context) {
//        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
//        if (packageInfo == null)
//            return null;
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//            } catch (NoSuchAlgorithmException e) {
//                Log.w("main", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//        return null;
//    }

}
