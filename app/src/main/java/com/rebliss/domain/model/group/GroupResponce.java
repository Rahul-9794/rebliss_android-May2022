package com.rebliss.domain.model.group;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class GroupResponce implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("desc")
    private String desc;

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


    public com.rebliss.domain.model.group.Data getData() {
        return data;
    }

    public void setData(com.rebliss.domain.model.group.Data data) {
        this.data = data;
    }

    @SerializedName("data")
    private Data data;

}
