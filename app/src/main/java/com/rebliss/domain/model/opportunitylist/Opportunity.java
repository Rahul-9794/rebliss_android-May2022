
package com.rebliss.domain.model.opportunitylist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Opportunity {
    @SerializedName("opportunity_id")
    @Expose
    private Integer opportunityId;
    @SerializedName("opportunity_tittle")
    @Expose
    private String opportunityTittle;
    @SerializedName("opportunity_description")
    @Expose
    private String opportunityDescription;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getOpportunityDescription() {
        return opportunityDescription;
    }

    public void setOpportunityDescription(String opportunityDescription) {
        this.opportunityDescription = opportunityDescription;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Integer opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOpportunityTittle() {
        return opportunityTittle;
    }

    public void setOpportunityTittle(String opportunityTittle) {
        this.opportunityTittle = opportunityTittle;
    }

}
