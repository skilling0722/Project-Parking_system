package com.example.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {
    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        mContext = getApplicationContext();
//        String key = getKeyHash(mContext);
//        Log.d("testt", "Key:" + key);
    }

    @BindView(R.id.button1) Button btn1;
    @BindView(R.id.button2) Button btn2;
    @BindView(R.id.button3) Button btn3;
    @BindView(R.id.firebase_test) Button firebase_test_btn;

    @OnClick(R.id.button1)
    void  button1Click() {
        Intent intent = new Intent(this, parking_check.class);
        startActivity(intent);
    }


    @OnClick(R.id.button2)
    void buttonClick2() {
        Intent intent = new Intent(this, menu_analysis.class);
        startActivity(intent);
    }

    @OnClick(R.id.button3)
    void buttonClick3() {
        Intent intent = new Intent(this, Data_analysis.class);
        startActivity(intent);
    }

    @OnClick(R.id.firebase_test)
    void buttonClick4() {
        Intent intent = new Intent(this, DB_test.class);
        startActivity(intent);
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("main", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }


}
