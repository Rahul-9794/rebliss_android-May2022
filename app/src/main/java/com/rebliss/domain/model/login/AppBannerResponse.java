package com.rebliss.domain.model.login;

import com.google.gson.annotations.SerializedName;

public class AppBannerResponse {


    @SerializedName("data")
    public Data data;
    @SerializedName("desc")
    public String desc;
    @SerializedName("status")
    public int status;

    public static class Data {
        @SerializedName("all_groups")
        public All_groups all_groups;
    }

    public static class All_groups {
        @SerializedName("is_active")
        public int is_active;
        @SerializedName("image")
        public String image;
        @SerializedName("id")
        public int id;
    }
}
