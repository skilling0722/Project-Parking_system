package com.example.parkingsystem;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class assistant {
    private boolean istts;

    public boolean isIstts() {
        return istts;
    }

    public void setIstts(boolean istts) {
        this.istts = istts;
    }

    public int generate_random_time() {
        Random random = new Random();
        int randomNum = random.nextInt(24);
        return randomNum;
    }

    public  String generate_random_month() {
        Random random = new Random();
        String month = "";
        int temp = random.nextInt(12) + 1;
        if (temp < 10) {
            month = "0" + temp;
        } else {
            month = "" + temp;
        }
        return month;
    }

    public String generate_random_day() {
        Random random = new Random();
        String day = "";
        int temp = random.nextInt(30) + 1;
        if (temp < 10) {
            day = "0" + temp;
        } else {
            day = "" + temp;
        }
        return day;
    }

    public String generate_random_str() {
        Random random = new Random();
        String randomStr = String.valueOf((char) ((int) (random.nextInt(26)) + 97));
        return randomStr;
    }

    public boolean generate_random_boolean() {
        return Math.random() < 0.5;
    }

    public HashMap<String, Integer> get_day() {
        long nowTime = System.currentTimeMillis();
        Date day = new Date(nowTime);
        SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time_format = new SimpleDateFormat("hh");
        int date = Integer.parseInt(date_format.format(day));
        int time = Integer.parseInt(time_format.format(day));

        //////////////////      날짜, 시간 랜덤 생성 테스트용
        time = generate_random_time();
        date = Integer.parseInt("2019" + generate_random_month() + generate_random_day() );
        //////////////////

        HashMap<String, Integer> map = new HashMap<>();
        map.put("date", date);
        Log.d("testt", "랜덤 날짜: " + date);
        map.put("time", time);

        return map;
    }

    public int get_dayofweek(String inputdate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = dateFormat.parse(inputdate);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day_num = calendar.get(Calendar.DAY_OF_WEEK);
        return day_num;
    }

    public HashMap<Integer, String> get_CWNU_spots(HashMap<Integer, String> map) {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        map.put(5, "5");
        map.put(6, "6");
        map.put(7, "7");
        map.put(8, "8");
        return map;
    }

    public ArrayList<String> get_position_list() {
        ArrayList position_list = new ArrayList();
        position_list.add("35.238501,128.692377");
        position_list.add("35.239641,128.690941");

        position_list.add("35.248158,128.687915");
        position_list.add("35.249779,128.687829");
        position_list.add("35.241596,128.702239");
        position_list.add("35.245149,128.690842");

        position_list.add("35.248597,128.693031");
        position_list.add("35.251090,128.685199");
        position_list.add("35.247550,128.686159");
        position_list.add("35.244019,128.681189");

        position_list.add("35.245863,128.677769");
        position_list.add("35.245115,128.670207");
        position_list.add("35.239705,128.683879");
        position_list.add("35.238724,128.678225");

        position_list.add("35.233976,128.689633");
        position_list.add("35.234318,128.688077");
        position_list.add("35.235852,128.684204");
        position_list.add("35.235396,128.680073");

        position_list.add("35.229074,128.693511");
        position_list.add("35.234472,128.684488");
        position_list.add("35.230343,128.678748");
        position_list.add("35.227784,128.682010");

        position_list.add("35.225085,128.687482");
        position_list.add("35.222491,128.692203");
        position_list.add("35.220650,128.694703");
        position_list.add("35.225085,128.699783");
        Collections.shuffle(position_list);             //중복 없이 랜덤 순서
        return position_list;
    }

}
