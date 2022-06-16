package com.rebliss.domain.model.forgotpassword;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForgotPasswordRequest implements Serializable {
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @SerializedName("phone_number")
    private String phone_number;

}
