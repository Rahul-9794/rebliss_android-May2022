package com.rebliss.domain.model;

import com.google.gson.annotations.SerializedName;

public class CudelResponse {
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
        @SerializedName("id")
        public int id;
        @SerializedName("app_version")
        public String app_version;
        @SerializedName("long")
        public String longitude;
        @SerializedName("lat")
        public String lat;
        @SerializedName("pincode")
        public String pincode;
        @SerializedName("state")
        public String state;
        @SerializedName("city")
        public String city;
        @SerializedName("address")
        public String address;
        @SerializedName("activity_status")
        public String activity_status;
        @SerializedName("sub_category1_id")
        public String sub_category1_id;
        @SerializedName("sub_category_id")
        public String sub_category_id;
        @SerializedName("category_id")
        public String category_id;
        @SerializedName("form_status")
        public String form_status;
        @SerializedName("shop_category")
        public String shop_category;
        @SerializedName("mobile_number")
        public String mobile_number;
        @SerializedName("business_name")
        public String business_name;
        @SerializedName("type")
        public String type;
        @SerializedName("activity_for")
        public String activity_for;
        @SerializedName("fos_id")
        public String fos_id;
    }
}
