package com.example.parkingsystem;

public class Data_for_analysis {
    private int date;
    private int time;
    private boolean use;

    public Data_for_analysis() {

    }

    public Data_for_analysis(int date, int time, boolean use) {
        this.date = date;
        this.time = time;
        this.use = use;
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
