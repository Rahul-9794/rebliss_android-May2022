package com.rebliss.domain.model.industrytype;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IndustryTypeResponse implements Serializable{
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


}
