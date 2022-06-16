
package com.rebliss.view.activity.cpdashboardforfos.dialogsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllGroup {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
