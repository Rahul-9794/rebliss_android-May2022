package com.rebliss.domain.model.changepassword;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChangePasswordRequest implements Serializable {
    @SerializedName("old_password")
    private String old_password;
    @SerializedName("password")
    private String password;

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    @SerializedName("confirm_password")
    private String confirm_password;

}
