package com.example.parkingsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.BaseAdapter;
import android.widget.Toast;

/*
* res - xml - *
* res - configs.xml 과 연결된다.
* 이 파일은 환경설정 화면과 관련된 제어 클래스다.
* 이 java클래스는 Config_Activity에서 사용됨.
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
            keywordVoicePreference.setSummary(prefs.getString("keyword_voice_list", "여성 밝은 대화체"));
        }

        if(prefs.getBoolean("voice_notify", true)){
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
//            if(key.equals("message")){
//                if(prefs.getBoolean("message",false)){
//                    Toast.makeText(getActivity(), "ON 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getActivity(), "OFF 되었을 때, 처리하는 이벤트", Toast.LENGTH_SHORT).show();
//                }
//            }

            //음성알림() 이벤트 처리
            if(key.equals("keyword_voice_list")){
                tmp = prefs.getString("keyword_voice_list", "여성 밝은 대화체");
                keywordVoicePreference.setSummary(tmp);
                if(tmp.equals("남성 차분한 낭독체")){
                    Toast.makeText(getActivity(), tmp + "로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(tmp.equals("남성 밝은 대화체")){
                    Toast.makeText(getActivity(), tmp + "로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(tmp.equals("여성 차분한 낭독체")){
                    Toast.makeText(getActivity(), tmp + "로 설정되었습니다.", Toast.LENGTH_SHORT).show();
            }
                else if(tmp.equals("여성 밝은 대화체")){
                    Toast.makeText(getActivity(), tmp + "로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }


            if(key.equals("voice_notify")){
                if(prefs.getBoolean("voice_notify", true)){
                    keywordScreen.setSummary("사용");
                }else{
                    keywordScreen.setSummary("사용안함");
                }
                //2뎁스 PreferenceScreen 내부에서 발생한 환경설정 내용을 2뎁스 PreferenceScreen에 적용하기 위한 소스
                ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            }

            //음성 명령(voice_command) 이벤트 처리
            if(key.equals("voice_command")){
                if(prefs.getBoolean("voice_command",true)){
                    Toast.makeText(getActivity(), "음성 조작이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "음성 조작이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            //주차 공간 확인 스타일 설정
            if(key.equals("check_style")){
                if(prefs.getBoolean("check_style",true)){
                    Toast.makeText(getActivity(), "지도 형식으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "리스트 형식으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
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
                    //System.out.println("색깔:녹색"+tmp);
                    getActivity().setTheme(R.style.greenTheme);
                    getActivity().getApplication().setTheme(R.style.greenTheme);
                } else if (tmp.equals("연한 파란색")) {
                    getActivity().setTheme(R.style.blueTheme);
                    getActivity().getApplication().setTheme(R.style.blueTheme);
                } else if(tmp.equals("default")){
                    getActivity().setTheme(R.style.defaultTheme);
                    getActivity().getApplication().setTheme(R.style.defaultTheme);
                }
                else {
                    System.out.println("색깔:없음"+tmp);
                }
                getActivity().recreate();
            }
        }
    };
}
