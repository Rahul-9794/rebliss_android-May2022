package com.rebliss.domain.model.searchstate;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchStateData implements Serializable {

    public SearchState getDetails() {
        return details;
    }

    public void setDetails(SearchState details) {
        this.details = details;
    }

    @SerializedName("details")
    private SearchState details;
}
