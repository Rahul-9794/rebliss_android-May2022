package com.rebliss.domain.model.InsuranceAmount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsuranceAmountResponse

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
        private AllGroups allGroups;

        public AllGroups getAllGroups() {
            return allGroups;
        }

        public void setAllGroups(AllGroups allGroups) {
            this.allGroups = allGroups;
        }

        public class AllGroups {

            @SerializedName("value")
            @Expose
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

    }
}
