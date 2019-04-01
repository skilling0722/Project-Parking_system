package com.example.parkingsystem;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class assistant {

    public int generate_random_num() {
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
        time = generate_random_num();
        date = Integer.parseInt("2019" + generate_random_month() + generate_random_day() );
        //////////////////

        HashMap<String, Integer> map = new HashMap<>();
        map.put("date", date);
        Log.d("testt", "랜덤 날짜: " + date);
        map.put("time", time);

        return map;
    }

    public int get_dayofweek(String inputdate) throws ParseException {
//        String dayofweek = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = dateFormat.parse(inputdate);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day_num = calendar.get(Calendar.DAY_OF_WEEK);

//        switch (day_num) {
//            case 1:
//                dayofweek = "Sun";
//                break;
//            case 2:
//                dayofweek = "Mon";
//                break;
//            case 3:
//                dayofweek = "Tue";
//                break;
//            case 4:
//                dayofweek = "Wed";
//                break;
//            case 5:
//                dayofweek = "Thu";
//                break;
//            case 6:
//                dayofweek = "Fri";
//                break;
//            case 7:
//                dayofweek = "Sat";
//                break;
//        }
        return day_num;
    }

}
