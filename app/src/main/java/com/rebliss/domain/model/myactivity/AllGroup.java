
package com.rebliss.domain.model.myactivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllGroup {

    public String getActivity_status() {
        return activity_status;
    }

    public void setActivity_status(String activity_status) {
        this.activity_status = activity_status;
    }

    public String get_long() {
        return _long;
    }

    public void set_long(String _long) {
        this._long = _long;
    }

    @SerializedName("activity_status")
    @Expose
    private String activity_status;

    @SerializedName("activity_detail_id")
    @Expose
    private Integer activityDetailId;
    @SerializedName("fos_id")
    @Expose
    private Integer fosId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("shop_category")
    @Expose
    private String shopCategory;
    @SerializedName("inside_photo")
    @Expose
    private String insidePhoto;
    @SerializedName("outside_photo")
    @Expose
    private String outsidePhoto;
    @SerializedName("activity_photo")
    @Expose
    private String activityPhoto;
    @SerializedName("pincode")
    @Expose
    private Integer pincode;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("gst_number")
    @Expose
    private String gstNumber;
    @SerializedName("gst_photo")
    @Expose
    private Object gstPhoto;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private Integer subCategoryId;
    @SerializedName("sub_category1_id")
    @Expose
    private Integer subCategory1Id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reason_to_decline")
    @Expose
    private Object reasonToDecline;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getActivityDetailId() {
        return activityDetailId;
    }

    public void setActivityDetailId(Integer activityDetailId) {
        this.activityDetailId = activityDetailId;
    }

    public Integer getFosId() {
        return fosId;
    }

    public void setFosId(Integer fosId) {
        this.fosId = fosId;
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

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public String getInsidePhoto() {
        return insidePhoto;
    }

    public void setInsidePhoto(String insidePhoto) {
        this.insidePhoto = insidePhoto;
    }

    public String getOutsidePhoto() {
        return outsidePhoto;
    }

    public void setOutsidePhoto(String outsidePhoto) {
        this.outsidePhoto = outsidePhoto;
    }

    public String getActivityPhoto() {
        return activityPhoto;
    }

    public void setActivityPhoto(String activityPhoto) {
        this.activityPhoto = activityPhoto;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public Object getGstPhoto() {
        return gstPhoto;
    }

    public void setGstPhoto(Object gstPhoto) {
        this.gstPhoto = gstPhoto;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Integer getSubCategory1Id() {
        return subCategory1Id;
    }

    public void setSubCategory1Id(Integer subCategory1Id) {
        this.subCategory1Id = subCategory1Id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getReasonToDecline() {
        return reasonToDecline;
    }

    public void setReasonToDecline(Object reasonToDecline) {
        this.reasonToDecline = reasonToDecline;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
