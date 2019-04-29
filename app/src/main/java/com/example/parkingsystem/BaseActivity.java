package com.example.parkingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.AudioManager;
import android.view.KeyEvent;

/*
    각 액티비티에 공통적으로 적용이 되도록 하는 베이스 엑티비티를 생성한다.
    소리설정
* */
public class BaseActivity extends AppCompatActivity {

    /* 폰 왼쪽에 키누르면 미디어소리 조절 가능하게 설정하는 함수 */
    @Override
    public boolean onKeyDown(int key_code, KeyEvent event){
        switch(key_code){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                break;
        }
        return super.onKeyDown(key_code, event);
    }
}
