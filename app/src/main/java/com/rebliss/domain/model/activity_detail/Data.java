
package com.rebliss.domain.model.activity_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("all_groups")
    @Expose
    private AllGroups allGroups;
    @SerializedName("inside_photos")
    @Expose
    private List<InsidePhoto> insidePhotos = null;
    @SerializedName("outside_photos")
    @Expose
    private List<OutsidePhoto> outsidePhotos = null;
    @SerializedName("activity_photos")
    @Expose
    private List<ActivityPhoto> activityPhotos = null;

    public AllGroups getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(AllGroups allGroups) {
        this.allGroups = allGroups;
    }

    public List<InsidePhoto> getInsidePhotos() {
        return insidePhotos;
    }

    public void setInsidePhotos(List<InsidePhoto> insidePhotos) {
        this.insidePhotos = insidePhotos;
    }

    public List<OutsidePhoto> getOutsidePhotos() {
        return outsidePhotos;
    }

    public void setOutsidePhotos(List<OutsidePhoto> outsidePhotos) {
        this.outsidePhotos = outsidePhotos;
    }

    public List<ActivityPhoto> getActivityPhotos() {
        return activityPhotos;
    }

    public void setActivityPhotos(List<ActivityPhoto> activityPhotos) {
        this.activityPhotos = activityPhotos;
    }

   public class ActivityPhoto {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("key")
        @Expose
        private Integer key;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }
}
   public class InsidePhoto {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("key")
        @Expose
        private Integer key;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }
    }

   public class OutsidePhoto {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("key")
        @Expose
        private Integer key;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }
    }



}





