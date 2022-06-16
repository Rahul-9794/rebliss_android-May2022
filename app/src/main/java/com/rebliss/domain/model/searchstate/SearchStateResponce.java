package com.rebliss.domain.model.searchstate;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchStateResponce implements Serializable {
    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private SearchStateData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SearchStateData getData() {
        return data;
    }

    public void setData(SearchStateData data) {
        this.data = data;
    }
}
