
package com.rebliss.domain.model.shopcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllShop {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("value")
    @Expose
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
