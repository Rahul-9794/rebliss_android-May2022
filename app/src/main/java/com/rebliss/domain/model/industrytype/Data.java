package com.rebliss.domain.model.industrytype;

import com.google.gson.annotations.SerializedName;
import com.rebliss.domain.model.industrytype.IndustryType;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    public List<IndustryType> getIndustryType() {
        return industry;
    }

    public void setIndustryType(List<IndustryType> industry) {
        this.industry = industry;
    }

    @SerializedName("industry")
    private List<IndustryType> industry;
}
