
package com.rebliss.domain.model.opportunityvideo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("all_groups")
    @Expose
    private List<AllGroup> allGroups = null;

    public List<AllGroup> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<AllGroup> allGroups) {
        this.allGroups = allGroups;
    }

}
