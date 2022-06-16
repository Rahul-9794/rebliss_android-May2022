package com.rebliss.domain.model.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Log_inRequest implements Serializable {


    @SerializedName("phone_number")
    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }
    public String getDevice_type() {
        return device_type;
    }
    public String getDevice_id() {
        return device_id;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    @SerializedName("device_id")
    private String device_id;
    @SerializedName("device_type")
    private String device_type;
    @SerializedName("password")
    private String password;

    private int isNew;

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
