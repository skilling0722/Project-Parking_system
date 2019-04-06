package com.example.parkingsystem;

public class recyclerview_item {
    private String spot_name;
    private boolean use;

    public recyclerview_item(String spot_name, boolean use) {
        this.spot_name = spot_name;
        this.use = use;
    }

    public String getSpot_name() {
        return spot_name;
    }

    public void setSpot_name(String spot_name) {
        this.spot_name = spot_name;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}
