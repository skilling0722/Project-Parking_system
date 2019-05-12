package com.example.parkingsystem;
/*
분석용 데이터 클래스
 */
public class Data_for_analysis {
    private int date;
    private int time;
    private boolean use;
    private String type;
    private String weather;

    public Data_for_analysis() {

    }

    /*이거 써야됌 */
    public Data_for_analysis(int date, int time, boolean use, String type, String weather) {
        this.date = date;
        this.time = time;
        this.use = use;
        this.type = type;
        this.weather = weather;
    }

    public int getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public boolean isUse() {
        return use;
    }

    public String getWeather() { return weather; }

    public String getType() { return type; }

    public void setDate(int date) {
        this.date = date;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}
