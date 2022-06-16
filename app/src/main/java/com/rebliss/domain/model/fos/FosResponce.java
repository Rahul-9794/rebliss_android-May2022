package com.rebliss.domain.model.fos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FosResponce implements Serializable {
    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<Data> data;

}
