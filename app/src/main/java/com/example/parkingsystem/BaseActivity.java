package com.example.parkingsystem;

/*
    각 액티비티에 공통적으로 적용이 되도록 하는 베이스 엑티비티를 생성한다.
    소리설정
    로딩-액티비티에 사용할
* */
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.widget.ImageView;
/* 설정 미디어로 되게 */
import android.media.AudioManager;
import android.view.KeyEvent;

public class BaseActivity extends AppCompatActivity {
    private AppCompatDialog loading_dialog;
    public void show_loading() {
        if (loading_dialog == null) {
//            Log.d("testt", "create loading_dialog");
            loading_dialog = new AppCompatDialog(this);
            loading_dialog.setCancelable(false);
        }

        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_dialog.setContentView(R.layout.progress_loading);
        loading_dialog.show();

        ImageView frame_loading = (ImageView) loading_dialog.findViewById(R.id.loading_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) frame_loading.getDrawable();
        animationDrawable.start();
    }

    public void hide_loading() {
        if (loading_dialog != null && loading_dialog.isShowing()) {
            loading_dialog.dismiss();
        }
    }
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
