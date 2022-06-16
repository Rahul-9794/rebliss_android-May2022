package com.rebliss.domain.model;

public class ActivitySelectModel {

    Integer category,subCategory,subCategory1;
    String earningTaskID, amount;

    public String getEarningTaskID() {
        return earningTaskID;
    }

    public void setEarningTaskID(String earningTaskActivity) {
        this.earningTaskID = earningTaskActivity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Integer subCategory) {
        this.subCategory = subCategory;
    }

    public Integer getSubCategory1() {
        return subCategory1;
    }

    public void setSubCategory1(Integer subCategory1) {
        this.subCategory1 = subCategory1;
    }
}
