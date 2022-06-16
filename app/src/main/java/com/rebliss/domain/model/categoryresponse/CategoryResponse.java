
package com.rebliss.domain.model.categoryresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("isexists")
    @Expose
    private String isexists;

    @SerializedName("message")
    @Expose
    private String message;

    public String getIsexists() {
        return isexists;
    }

    public void setIsexists(String isexists) {
        this.isexists = isexists;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
