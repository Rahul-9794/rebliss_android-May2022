package com.rebliss.domain.model.state;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class State implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("fk_c_country_code")
    private String fk_c_country_code;
    @SerializedName("s_name")
    private String s_name;
    @SerializedName("state_abbrev_code")
    private String state_abbrev_code;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFk_c_country_code() {
        return fk_c_country_code;
    }
    public void setFk_c_country_code(String fk_c_country_code) { this.fk_c_country_code = fk_c_country_code; }
    public String getS_name() {
        return s_name;
    }
    public void setS_name(String s_name) {
        this.s_name = s_name;
    }
    public String getState_abbrev_code() {
        return state_abbrev_code;
    }
    public void setState_abbrev_code(String state_abbrev_code) {
        this.state_abbrev_code = state_abbrev_code;
    }
}
