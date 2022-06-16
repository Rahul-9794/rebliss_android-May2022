
package com.rebliss.domain.model.employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {


    @SerializedName("total_approved")
    @Expose
    private Integer totalApproved;
    @SerializedName("total_declined")
    @Expose
    private Integer totalDeclined;
    @SerializedName("total_pending")
    @Expose
    private Integer totalPending;
    @SerializedName("total_resubmitted")
    @Expose
    private Integer totalResubmitted;


    public Integer getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(Integer totalPending) {
        this.totalPending = totalPending;
    }

    public Integer getTotalResubmitted() {
        return totalResubmitted;
    }

    public void setTotalResubmitted(Integer totalResubmitted) {
        this.totalResubmitted = totalResubmitted;
    }

    public Integer getTotalApproved() {
        return totalApproved;
    }

    public void setTotalApproved(Integer totalApproved) {
        this.totalApproved = totalApproved;
    }

    public Integer getTotalDeclined() {
        return totalDeclined;
    }

    public void setTotalDeclined(Integer totalDeclined) {
        this.totalDeclined = totalDeclined;
    }

}
