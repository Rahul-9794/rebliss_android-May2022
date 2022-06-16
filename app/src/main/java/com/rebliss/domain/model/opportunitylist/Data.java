
package com.rebliss.domain.model.opportunitylist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("opportunity")
    @Expose
    private List<Opportunity> opportunity = null;

    public List<Opportunity> getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(List<Opportunity> opportunity) {
        this.opportunity = opportunity;
    }

}
