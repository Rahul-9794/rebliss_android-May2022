package com.rebliss.domain.model.city;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("fk_c_country_code")
    private String fk_c_country_code;
    @SerializedName("s_name")
    private String s_name;

    public String getCity_abbrev_code() {
        return city_abbrev_code;
    }

    public void setCity_abbrev_code(String city_abbrev_code) {
        this.city_abbrev_code = city_abbrev_code;
    }

    public String getS_name_hi() {
        return s_name_hi;
    }

    public void setS_name_hi(String s_name_hi) {
        this.s_name_hi = s_name_hi;
    }

    @SerializedName("city_abbrev_code")
    private String city_abbrev_code;
    @SerializedName("s_name_hi")
    private String s_name_hi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFk_c_country_code() {
        return fk_c_country_code;
    }

    public void setFk_c_country_code(String fk_c_country_code) {
        this.fk_c_country_code = fk_c_country_code;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }




}
