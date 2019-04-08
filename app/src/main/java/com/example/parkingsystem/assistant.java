package com.example.parkingsystem;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        map.put(1, "본부 주차장");
        map.put(2, "인문대 주차장");
        map.put(3, "자연대 주차장");
        map.put(4, "공대 주차장");
        map.put(5, "정문 주차장");
        map.put(6, "후문 주차장");
        map.put(7, "도서관 주차장");
        map.put(8, "학군단 주차장");
        return map;
    }

}
