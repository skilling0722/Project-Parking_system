package com.example.parkingsystem;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class assistant {

    public int generate_random_num() {
        Random random = new Random();
        int randomNum = random.nextInt(24);
        return randomNum;
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

        //////////////////      타임 랜덤 생성 테스트용
        time = generate_random_num();
        //////////////////
        HashMap<String, Integer> map = new HashMap<>();
        map.put("date", date);
        map.put("time", time);

        return map;
    }

}
