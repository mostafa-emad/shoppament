package com.shoppament.data.models;

public class SlotTimingModel extends BaseModel{
    private String id;
    private String time;

    public SlotTimingModel(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
