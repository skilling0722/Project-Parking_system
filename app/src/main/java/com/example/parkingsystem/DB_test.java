package com.example.parkingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/*
데이터베이스에 샘플 데이터 생성용 클래스
 */
public class DB_test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config_Activity.setBackground(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);
        ButterKnife.bind(this);
    }

    @BindView(R.id.write_check_btn) Button write_check_btn;
    @BindView(R.id.get_check_btn) Button get_check_btn;
    @BindView(R.id.write_analysis_btn) Button write_analysis_btn;
    @BindView(R.id.get_analysis_btn) Button get_analysis_btn;
    @BindView(R.id.write_changwon_btn) Button write_changwon_btn;

    @OnClick(R.id.write_check_btn)
    void  write_check_btnClick() {
        Firebase_assist firebase_assist = new Firebase_assist();
        assistant random = new assistant();
        for (int i = 0; i< 30; i++) {
            String str = random.generate_random_str();              //주차장 랜덤 생성
            boolean bool = random.generate_random_boolean();        //사용여부 랜덤 생성
            int num = random.generate_random_time();                //주차면 랜덤 생성

            try {
                firebase_assist.writeDB_for_check("check", str, Integer.toString(num), bool);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("testt", "fail check write");
            }
        }
    }

    @OnClick(R.id.get_check_btn)
    void get_check_btnClick() {
        Firebase_assist firebase_assist = new Firebase_assist();
        firebase_assist.getDB_for_check("check");
    }

    @OnClick(R.id.write_analysis_btn)
    void write_analysis_btnClick() {
        Firebase_assist fire_assistant = new Firebase_assist();
        assistant random = new assistant();

        for ( int i = 0; i < 500; i++) {
            String str = random.generate_random_str();
            HashMap<String, Integer> Date = random.get_day();
            boolean bool = random.generate_random_boolean();
            try {
                fire_assistant.writeDB_for_analysis("analysis", str, Date.get("date"), Date.get("time"), bool);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.get_analysis_btn)
    void get_analysis_btnClick() {
        Firebase_assist fire_assistant = new Firebase_assist();
        fire_assistant.getDB_for_analysis("analysis");
    }


    /*
    주차장 프로토타입
     */
    @OnClick(R.id.write_changwon_btn)
    void write_changwon_btnClick() {
        Firebase_assist fire_assistant = new Firebase_assist();
        assistant assistant = new assistant();
        HashMap<Integer, String> CWNU_spots = new HashMap<>();
        CWNU_spots = assistant.get_CWNU_spots(CWNU_spots);

        for ( int i = 0; i < 8; i++ ) {
            String str = "55호관";
            boolean bool = assistant.generate_random_boolean();
            try {
            fire_assistant.writeDB_for_check("check", str, CWNU_spots.get(i+1), bool);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         fire_assistant.writeDB_position_for_check("check", "55호관", "35.241453,128.695744");
    }
}
