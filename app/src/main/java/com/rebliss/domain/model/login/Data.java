package com.rebliss.domain.model.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    @SerializedName("token")
    private String token;
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("device_type")
    private String device_type;
    @SerializedName("mobile_verified")
    private int mobile_verified;
    @SerializedName("group_id")
    private int group_id = -1;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getGroup_detail_id() {
        return group_detail_id;
    }

    public void setGroup_detail_id(int group_detail_id) {
        this.group_detail_id = group_detail_id;
    }

    @SerializedName("group_detail_id")
    private int group_detail_id;
    @SerializedName("message")
    private String message;
    @SerializedName("fos_type")
    private String fos_type;

    @SerializedName("code")
    private String code;

    public String getProfile_verified() {
        return profile_verified;
    }

    public void setProfile_verified(String profile_verified) {
        this.profile_verified = profile_verified;
    }

    @SerializedName("profile_verified")
    private String profile_verified;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public int getMobile_verified() {
        return mobile_verified;
    }

    public void setMobile_verified(int mobile_verified) {
        this.mobile_verified = mobile_verified;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public validationError getValidation_error() {
        return validation_error;
    }

    public void setValidation_error(validationError validation_error) {
        this.validation_error = validation_error;
    }

    @SerializedName("validation_error")
    private validationError validation_error;

    public String getFos_type() {
        return fos_type;
    }

    public void setFos_type(String fos_type) {
        this.fos_type = fos_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
