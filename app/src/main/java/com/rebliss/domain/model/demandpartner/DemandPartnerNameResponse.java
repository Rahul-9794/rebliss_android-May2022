package com.rebliss.domain.model.demandpartner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DemandPartnerNameResponse
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

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

    public class AllGroup {

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
        private Integer parentId;

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

        public Integer getParentId() {
            return parentId;
        }

        public void setParentId(Integer parentId) {
            this.parentId = parentId;
        }

    }
}
