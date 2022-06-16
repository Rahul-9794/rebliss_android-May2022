package com.rebliss.domain.model.fos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("email")
    private String email;
    @SerializedName("auth_key")
    private String auth_key;

    @SerializedName("region_master_id")
    private String region_master_id;
    @SerializedName("password_hash")
    private String password_hash;
    @SerializedName("password_reset_token")
    private String password_reset_token;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("group_id")
    private String group_id;
    @SerializedName("group_detail_id")
    private String group_detail_id;
    @SerializedName("img_path")
    private String img_path;
    @SerializedName("accept_tnc")
    private String accept_tnc;
    @SerializedName("invite_user_id")
    private String invite_user_id;
    @SerializedName("status")
    private String status;
    @SerializedName("group")
    private String group;
    @SerializedName("email_verified")
    private String email_verified;
    @SerializedName("sms_verified")
    private String sms_verified;
    @SerializedName("profile_verified")
    private String profile_verified;
    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("current_points")
    private String current_points;
    @SerializedName("notification_count")
    private String notification_count;
    @SerializedName("last_login")
    private String last_login;
    @SerializedName("by_admin")
    private String by_admin;
    @SerializedName("code")
    private String code;
    @SerializedName("created")
    private String created;

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

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public String getRegion_master_id() {
        return region_master_id;
    }

    public void setRegion_master_id(String region_master_id) {
        this.region_master_id = region_master_id;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPassword_reset_token() {
        return password_reset_token;
    }

    public void setPassword_reset_token(String password_reset_token) {
        this.password_reset_token = password_reset_token;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_detail_id() {
        return group_detail_id;
    }

    public void setGroup_detail_id(String group_detail_id) {
        this.group_detail_id = group_detail_id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getAccept_tnc() {
        return accept_tnc;
    }

    public void setAccept_tnc(String accept_tnc) {
        this.accept_tnc = accept_tnc;
    }

    public String getInvite_user_id() {
        return invite_user_id;
    }

    public void setInvite_user_id(String invite_user_id) {
        this.invite_user_id = invite_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public String getSms_verified() {
        return sms_verified;
    }

    public void setSms_verified(String sms_verified) {
        this.sms_verified = sms_verified;
    }

    public String getProfile_verified() {
        return profile_verified;
    }

    public void setProfile_verified(String profile_verified) {
        this.profile_verified = profile_verified;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCurrent_points() {
        return current_points;
    }

    public void setCurrent_points(String current_points) {
        this.current_points = current_points;
    }

    public String getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(String notification_count) {
        this.notification_count = notification_count;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getBy_admin() {
        return by_admin;
    }

    public void setBy_admin(String by_admin) {
        this.by_admin = by_admin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @SerializedName("modified")
    private String modified;

}
