
package com.rebliss.domain.model.myactivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyActivityDashboardResponse {

    @SerializedName("status")
    private Integer status;
    @SerializedName("desc")
    private String desc;
    @SerializedName("data")
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
