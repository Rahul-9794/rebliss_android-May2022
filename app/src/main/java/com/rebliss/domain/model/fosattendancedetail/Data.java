
package com.rebliss.domain.model.fosattendancedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("attendance_id")
    @Expose
    private Integer attendanceId;
    @SerializedName("fos_id")
    @Expose
    private Integer fosId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("opportunity_id")
    @Expose
    private Object opportunityId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Integer getFosId() {
        return fosId;
    }

    public void setFosId(Integer fosId) {
        this.fosId = fosId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Object opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}
