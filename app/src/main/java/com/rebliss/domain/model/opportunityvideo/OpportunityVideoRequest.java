package com.rebliss.domain.model.opportunityvideo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OpportunityVideoRequest implements Serializable {
    public String getOpportunity_title() {
        return opportunity_title;
    }

    public void setOpportunity_title(String opportunity_title) {
        this.opportunity_title = opportunity_title;
    }

    @SerializedName("opportunity_title")
    private String opportunity_title;
}

