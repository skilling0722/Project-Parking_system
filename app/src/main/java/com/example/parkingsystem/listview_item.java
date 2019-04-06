package com.example.parkingsystem;

import android.graphics.drawable.Drawable;

public class listview_item {
    private Drawable icon;
    private String spot_title;
    private String remain_title;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getSpot_title() {
        return spot_title;
    }

    public void setSpot_title(String spot_title) {
        this.spot_title = spot_title;
    }

    public String getRemain_title() {
        return remain_title;
    }
//
    public void setRemain_title(String remain_title) {
        this.remain_title = remain_title;
    }
}
