
package com.rebliss.domain.model.categoryresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCategory {

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("category_type")
    @Expose
    private String categoryType;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("parent_id")
    @Expose
    private Object parentId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

}
