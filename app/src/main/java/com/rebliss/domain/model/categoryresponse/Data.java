
package com.rebliss.domain.model.categoryresponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("all_category")
    @Expose
    private List<AllCategory> allCategory = null;

    @SerializedName("all_groups")
    @Expose
    private List<AllCategory> allGroups = null;

    public List<AllCategory> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<AllCategory> allGroups) {
        this.allGroups = allGroups;
    }

    public List<AllCategory> getAllCategory() {
        return allCategory;
    }

    public void setAllCategory(List<AllCategory> allCategory) {
        this.allCategory = allCategory;
    }

}
