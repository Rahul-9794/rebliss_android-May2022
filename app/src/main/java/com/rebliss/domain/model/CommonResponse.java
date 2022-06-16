
package com.rebliss.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rebliss.domain.model.opportunitylist.Data;

public class CommonResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("desc")
    @Expose
    private String desc;

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


}
