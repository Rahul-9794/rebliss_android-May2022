package com.rebliss.domain.model.approvedecline;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApproveRequest implements Serializable {

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @SerializedName("user_id")
    private String user_id;

    public String getDecline_message() {
        return decline_message;
    }

    public void setDecline_message(String decline_message) {
        this.decline_message = decline_message;
    }

    @SerializedName("decline_message")
    private String decline_message;
}
