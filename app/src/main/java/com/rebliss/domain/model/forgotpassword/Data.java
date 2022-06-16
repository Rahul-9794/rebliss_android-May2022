package com.rebliss.domain.model.forgotpassword;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;

}
