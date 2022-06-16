
package com.rebliss.domain.model.termsandconditions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("all_groups")
    @Expose
    private AllGroups allGroups;

    public AllGroups getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(AllGroups allGroups) {
        this.allGroups = allGroups;
    }

}
