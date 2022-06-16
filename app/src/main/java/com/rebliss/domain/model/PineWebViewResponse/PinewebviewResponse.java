package com.rebliss.domain.model.PineWebViewResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PinewebviewResponse
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

    }
    public class AllGroups {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("landing_url")
        @Expose
        private String landingUrl;
        @SerializedName("destination_url")
        @Expose
        private String destinationUrl;
        @SerializedName("extra_url_one")
        @Expose
        private String extraUrlOne;
        @SerializedName("extra_url_two")
        @Expose
        private String extraUrlTwo;
        @SerializedName("created_at")
        @Expose
        private Object createdAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLandingUrl() {
            return landingUrl;
        }

        public void setLandingUrl(String landingUrl) {
            this.landingUrl = landingUrl;
        }

        public String getDestinationUrl() {
            return destinationUrl;
        }

        public void setDestinationUrl(String destinationUrl) {
            this.destinationUrl = destinationUrl;
        }

        public String getExtraUrlOne() {
            return extraUrlOne;
        }

        public void setExtraUrlOne(String extraUrlOne) {
            this.extraUrlOne = extraUrlOne;
        }

        public String getExtraUrlTwo() {
            return extraUrlTwo;
        }

        public void setExtraUrlTwo(String extraUrlTwo) {
            this.extraUrlTwo = extraUrlTwo;
        }

        public Object getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Object createdAt) {
            this.createdAt = createdAt;
        }

    }
}
