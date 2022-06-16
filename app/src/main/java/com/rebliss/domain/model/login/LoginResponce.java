package com.rebliss.domain.model.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponce implements Serializable {
    @SerializedName("status")
    private int status;

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

    @SerializedName("data")
    private Data data;

}
