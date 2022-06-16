
package com.rebliss.domain.model.myactivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("all_groups")
    private List<AllGroup> allGroups = null;
    @SerializedName("cuDelData")
    private List<AllGroup> cuDelData = null;
    @SerializedName("total")
    private Integer total;
    @SerializedName("cuDelDataCount")
    private Integer cuDelDataCount;

    public List<AllGroup> getCuDelData() {
        return cuDelData;
    }

    @SerializedName("dsrData")
    private List<DsrDatum> dsrData = null;
    @SerializedName("dsrDataCount")
    private Integer dsrDataCount;

    public void setCuDelData(List<AllGroup> cuDelData) {
        this.cuDelData = cuDelData;
    }

    public Integer getCuDelDataCount() {
        return cuDelDataCount;
    }

    public void setCuDelDataCount(Integer cuDelDataCount) {
        this.cuDelDataCount = cuDelDataCount;
    }

    public List<AllGroup> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<AllGroup> allGroups) {
        this.allGroups = allGroups;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DsrDatum> getDsrData() {
        return dsrData;
    }

    public void setDsrData(List<DsrDatum> dsrData) {
        this.dsrData = dsrData;
    }

    public Integer getDsrDataCount() {
        return dsrDataCount;
    }

    public void setDsrDataCount(Integer dsrDataCount) {
        this.dsrDataCount = dsrDataCount;
    }


}
