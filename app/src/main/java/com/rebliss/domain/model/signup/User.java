package com.rebliss.domain.model.signup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("group_id")
    private String group_id;
    @SerializedName("shop_name")
    private String shop_name;
    public String getProfile_verified() {
        return profile_verified;
    }

    public void setProfile_verified(String profile_verified) {
        this.profile_verified = profile_verified;
    }

    @SerializedName("profile_verified")
    private String profile_verified;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public int getGroup_detail_id() {
        return group_detail_id;
    }

    public void setGroup_detail_id(int group_detail_id) {
        this.group_detail_id = group_detail_id;
    }

    @SerializedName("group_detail_id")
    private int group_detail_id;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
