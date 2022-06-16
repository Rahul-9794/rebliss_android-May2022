package com.rebliss.domain.model.fospos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetFosPos implements Serializable {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
