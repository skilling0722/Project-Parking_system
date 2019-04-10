package com.example.parkingsystem;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity implements speech_recognition_service.ServiceCallbacks {

    private static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 0;
    private speech_recognition_service srs_service;
    private boolean isService;


    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            speech_recognition_service.speech_recognition_binder srs_binder = (speech_recognition_service.speech_recognition_binder) service;
            srs_service = srs_binder.getService();  //start_service 호출하면 되드라
            isService = true;
            srs_service.setCallbacks(MainActivity.this);
        }
        public void onServiceDisconnected(ComponentName name) {
            srs_service = null;
            isService = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        assistant assistant = new assistant();
        assistant.setIstts(false);

        start_service();
//        mContext = getApplicationContext();
//        String key = getKeyHash(mContext);
//        Log.d("testt", "Key:" + key);
    }

    @BindView(R.id.button1) Button btn1;
    @BindView(R.id.button2) Button btn2;
    @BindView(R.id.button3) Button btn3;
    @BindView(R.id.firebase_test) Button firebase_test_btn;
    @BindView(R.id.newton_test) Button newton_test_btn;
    @BindView(R.id.newtontalk_test) Button newtontalk_test_btn;

    @BindView(R.id.test_btn1) Button test_btn11;
    @BindView(R.id.test_btn2) Button test_btn22;
    @BindView(R.id.test_btn3) Button test_btn33;

    @OnClick(R.id.test_btn1)
    void  button122Click() {
        start_service();
    }

    @OnClick(R.id.test_btn2)
    void  button13131Click() {
        use_service();
    }

    @OnClick(R.id.test_btn3)
    void  button1311131Click() {
        stop_service();
    }


    @OnClick(R.id.button1)
    void  Parking_check() {
        Intent intent = new Intent(this, parking_check.class);
        startActivity(intent);
    }


    @OnClick(R.id.button2)
    void Data_analysis() {
  //      Intent intent = new Intent(this, test.class);
//        startActivity(intent);
        Intent intent = new Intent(this, menu_analysis_Activity.class);

//        Intent intent = new Intent(this, Data_analysis_Activity.class);
        startActivity(intent);

    }

    @OnClick(R.id.button3)
    void config() {
        /*
        * Intent intent = new Intent(this, Data_analysis.class);
        * */
        /* 설정화면 */
        Intent intent = new Intent(this, Config_Activity.class);
        startActivity(intent);

    }

    @OnClick(R.id.firebase_test)
    void test_db() {
        Intent intent = new Intent(this, DB_test.class);
        startActivity(intent);
    }

    @OnClick(R.id.newton_test)
    void buttonClick5() {
        Intent intent = new Intent(this, newton_test.class);
        startActivity(intent);
    }

    @OnClick(R.id.newtontalk_test)
    void buttonClick6() {
        Intent intent = new Intent(this, newtontalk_test.class);
        startActivity(intent);
    }

    public void start_service() {
        try {
            Intent intent = new Intent(MainActivity.this, speech_recognition_service.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
            Log.d("testt", "start_service");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testt", "start_service error: " + e);
        }
    }

    public  void stop_service() {
        if (isService) {
            unbindService(conn);
            isService = false;
            Log.d("testt", "stop_service");
        }
        else {
            Log.d("testt", "중지할 서비스 없음");
        }
    }

    public void use_service() {
        SharedPreferences shared_tts = getSharedPreferences("istts", MODE_PRIVATE);
        Boolean istts = shared_tts.getBoolean("usage", true);
        Log.d("testt", "istts: " + istts);

        if( !isService ) {
            Log.d("testt", "음성 인식 서비스가 실행되고있지않아.");
            return;
        }
//        int num = srs_service.getRan();
//        Log.d("testt", "srs_service로 부터 받아온 값: " + num);
    }

    @Override
    public void do_something(String result) {
        Log.d("testt", "음성 인식 결과: " + result);

        if( result.equals("주차 공간 확인") ) {
            Parking_check();
        }
        else if ( result.equals("주차 정보 분석") ) {
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
        else {
            Log.d("testt", "음성 인식에 해당되는 것 없음");
        }
    }

//
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
