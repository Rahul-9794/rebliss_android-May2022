
package com.rebliss.domain.model.opportunityvideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllGroup {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("opportunity_title")
    @Expose
    private String opportunityTitle;



    @SerializedName("video_link")
    @Expose
    private String videoLink;

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("valid_till_date")
    @Expose
    private String validTillDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpportunityTitle() {
        return opportunityTitle;
    }

    public void setOpportunityTitle(String opportunityTitle) {
        this.opportunityTitle = opportunityTitle;
    }

    public String getValidTillDate() {
        return validTillDate;
    }

    public void setValidTillDate(String validTillDate) {
        this.validTillDate = validTillDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
