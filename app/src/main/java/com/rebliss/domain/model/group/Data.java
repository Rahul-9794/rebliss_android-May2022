package com.rebliss.domain.model.group;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    public List<AllGroups> getAll_groups() {
        return all_groups;
    }

    public void setAll_groups(List<AllGroups> all_groups) {
        this.all_groups = all_groups;
    }

    @SerializedName("all_groups")
    private List<AllGroups> all_groups;



}
