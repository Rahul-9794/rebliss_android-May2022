package com.rebliss.domain.model.changepassword;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChangePasswordResponce implements Serializable {
    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("data")
    private String message;

}
