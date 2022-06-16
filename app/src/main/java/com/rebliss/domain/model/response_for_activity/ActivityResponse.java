package com.rebliss.domain.model.response_for_activity;

import java.util.List;

public class ActivityResponse {
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Id> getId() {
        return id;
    }

    public void setId(List<Id> id) {
        this.id = id;
    }

    public int status;
    public String desc;
    public List<Id> id;
}
