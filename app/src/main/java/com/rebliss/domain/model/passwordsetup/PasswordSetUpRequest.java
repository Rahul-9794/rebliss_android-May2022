package com.rebliss.domain.model.passwordsetup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PasswordSetUpRequest implements Serializable {
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("password")
    private String password;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
