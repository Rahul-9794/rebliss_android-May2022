package com.rebliss.view.activity.cpdashboardforfos;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("all_groups")
    @Expose
    private List<AllGroup> allGroups = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<AllGroup> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<AllGroup> allGroups) {
        this.allGroups = allGroups;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
