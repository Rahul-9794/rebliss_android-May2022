package com.rebliss.domain.model.otp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtpRequest implements Serializable {
    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    @SerializedName("verify_code")
    private String verify_code;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @SerializedName("user_id")
    private String user_id;
}
