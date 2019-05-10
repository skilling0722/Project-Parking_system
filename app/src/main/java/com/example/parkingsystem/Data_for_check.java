package com.example.parkingsystem;

import java.util.HashMap;
/*
확인용 데이터 클래스
 */
public class Data_for_check {
    private boolean use;

    public  Data_for_check() {

    }

    public Data_for_check(boolean use) {
        this.use = use;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public HashMap<String, Boolean> convert_map() {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("use", use);
        return map;
    }
}
