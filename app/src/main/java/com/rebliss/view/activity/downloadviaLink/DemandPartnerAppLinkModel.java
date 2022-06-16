package com.rebliss.view.activity.downloadviaLink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DemandPartnerAppLinkModel implements Serializable
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
    private final static long serialVersionUID = 311532085390193638L;

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

    public class Data implements Serializable
    {

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

    public class AllGroup implements Serializable
    {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private Object createdAt;
        private final static long serialVersionUID = 7171068807786569694L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Object createdAt) {
            this.createdAt = createdAt;
        }

    }
}
