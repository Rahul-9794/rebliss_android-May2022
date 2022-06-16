package com.rebliss.domain.model.myactivity;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DsrDatum {
        @SerializedName("activity_detail_id")
        @Expose
        private Integer activityDetailId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("shop_name")
        @Expose
        private String shopName;
        @SerializedName("mobile")
        @Expose
        private String mobile;

        public int getActivityDetailId() {
            return activityDetailId;
        }

        public void setActivityDetailId(Integer activityDetailId) {
            this.activityDetailId = activityDetailId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

}
