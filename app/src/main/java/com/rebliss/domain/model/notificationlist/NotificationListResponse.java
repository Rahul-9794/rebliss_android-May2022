
package com.rebliss.domain.model.notificationlist;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationListResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("desc")
    @Expose
    private List<Desc> desc = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Desc> getDesc() {
        return desc;
    }

    public void setDesc(List<Desc> desc) {
        this.desc = desc;
    }

}
