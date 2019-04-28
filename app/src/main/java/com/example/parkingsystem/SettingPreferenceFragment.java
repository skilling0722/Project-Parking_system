package com.example.parkingsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.BaseAdapter;
import android.widget.Toast;

/*
* res - xml - *
* res - configs.xml 과 연결된다.
* 이 파일은 환경설정 화면과 관련된 제어 클래스다.
* 이 java클래스는 Config_Activity에서 사용됨.
*
* */
public class SettingPreferenceFragment extends PreferenceFragment {
    SharedPreferences prefs;
    Context context;
    ListPreference backgroundPreference;
    ListPreference keywordVoicePreference;
    PreferenceScreen keywordScreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);

        backgroundPreference = (ListPreference)findPreference("background_list");
        keywordVoicePreference = (ListPreference)findPreference("keyword_voice_list");
        keywordScreen = (PreferenceScreen)findPreference("voice_screen");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(!prefs.getString("background_list", "").equals("")){
            backgroundPreference.setSummary(prefs.getString("background_list", "검은 색"));
        }

        if(!prefs.getString("keyword_voice_list", "").equals("")){
            keywordVoicePreference.setSummary(prefs.getString("keyword_voice_list", "남자"));
        }

        if(prefs.getBoolean("voice_notify", false)){
            keywordScreen.setSummary("사용");
        }

    }// onCreate

    /*listener()등록/해제
    * 등록만 해주면 다시 실행할때 등록된게 여러개됨. 아니면 그 이상의 문제가 생길수가 있음.
    * 따라서 해제를 반드시 해주어야함. 프레그먼트에서.
    * */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(prefListener);

    }
    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(prefListener);
        super.onPause();
    }


    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String tmp = null;
            //메시지 알림(message) 이벤트 처리
            if(key.equals("message")){
                if(prefs.getBoolean("message",false)){
                    Toast.makeText(getActivity(), "ON 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "OFF 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
                }
            }

            //음성알림() 이벤트 처리
            if(key.equals("keyword_voice_list")){
                tmp = prefs.getString("keyword_voice_list", "남자");
                keywordVoicePreference.setSummary(tmp);
                if(tmp.equals("남자")){
                    Toast.makeText(getActivity(), "남자 선택 이벤트", Toast.LENGTH_SHORT).show();
                }
                else if(tmp.equals("여자")){
                    Toast.makeText(getActivity(), "여자 선택 이벤트", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "어린이 이벤트", Toast.LENGTH_SHORT).show();
                }
            }
            if(key.equals("voice_notify")){
                if(prefs.getBoolean("voice_notify", false)){
                    keywordScreen.setSummary("사용");
                }else{
                    keywordScreen.setSummary("사용안함");
                }
                //2뎁스 PreferenceScreen 내부에서 발생한 환경설정 내용을 2뎁스 PreferenceScreen에 적용하기 위한 소스
                ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            }

            //음성 명령(voice_command) 이벤트 처리
            if(key.equals("voice_command")){
                if(prefs.getBoolean("voice_command",false)){
                    Toast.makeText(getActivity(), "ON 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "OFF 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
                }
            }

            //소리(sound) 이벤트 처리
            if(key.equals("sound")){
                if(prefs.getBoolean("sound",false)){
                    Toast.makeText(getActivity(), "ON 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "OFF 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
                }
           }

           //배경색(background_list) 선택시 이벤트 처리
            if(key.equals("background_list")) {
                tmp = prefs.getString("background_list", "검은 색");
                backgroundPreference.setSummary(tmp);

                if(tmp.equals("검은 색")) {
                    getActivity().setTheme(R.style.blackTheme);
                    getActivity().getApplication().setTheme(R.style.blackTheme);
                } else if (tmp.equals("빨간 색")) {
                    System.out.println("색깔:빨간 색"+tmp);
                    getActivity().setTheme(R.style.redTheme);
                    getActivity().getApplication().setTheme(R.style.redTheme);
                } else if (tmp.equals("연한 녹색")) {
                    System.out.println("색깔:녹색"+tmp);
                    getActivity().setTheme(R.style.greenTheme);
                    getActivity().getApplication().setTheme(R.style.greenTheme);
                } else if (tmp.equals("연한 파란색")) {
                    getActivity().setTheme(R.style.blueTheme);
                    getActivity().getApplication().setTheme(R.style.blueTheme);
                } else {
                    System.out.println("색깔:없음"+tmp);
                }
                getActivity().recreate();
            }
        }
    };
}
