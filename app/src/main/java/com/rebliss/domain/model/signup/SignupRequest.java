package com.rebliss.domain.model.signup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignupRequest implements Serializable {

    @SerializedName("first_name")
    private String first_name;
    @SerializedName("current_service")
    private String current_service;
    @SerializedName("shop_photo")
    private String shop_photo;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("password")
    private String password;
    @SerializedName("confirm_password")
    private String confirm_password;
    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("group_id")
    private String group_id;
    @SerializedName("fos_shop_name")
    private String fos_shop_name;
    @SerializedName("fos_type")
    private String fos_type;

    //added by prashant
    @SerializedName("age_range")
    private String age_range;
    @SerializedName("occupation")
    private String occupation;
    @SerializedName("gender")
    private String gender;
    @SerializedName("location_zipcode")
    private String location_zipcode;
    @SerializedName("location_city")
    private String location_city;
    @SerializedName("location_state")
    private String location_state;
    private String education;

    public String getLocation_city() {
        return location_city;
    }

    public void setLocation_city(String location_city) {
        this.location_city = location_city;
    }

    public String getLocation_state() {
        return location_state;
    }

    public void setLocation_state(String location_state) {
        this.location_state = location_state;
    }

    public String getLocation_zipcode() {
        return location_zipcode;
    }

    public void setLocation_zipcode(String location_zipcode) {
        this.location_zipcode = location_zipcode;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @SerializedName("code")
    private String code;

    public String getGroup_detail_id() {
        return group_detail_id;
    }

    public void setGroup_detail_id(String group_detail_id) {
        this.group_detail_id = group_detail_id;
    }

    @SerializedName("group_detail_id")
    private String group_detail_id;


    public String getAge_range() {
        return age_range;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }

    public String getFos_shop_name() {
        return fos_shop_name;
    }

    public void setFos_shop_name(String fos_shop_name) {
        this.fos_shop_name = fos_shop_name;
    }

    public String getFos_type() {
        return fos_type;
    }

    public void setFos_type(String fos_type) {
        this.fos_type = fos_type;
    }


    public String getShop_photo() {
        return shop_photo;
    }

    public void setShop_photo(String shop_photo) {
        this.shop_photo = shop_photo;
    }

    public String getCurrent_service() {
        return current_service;
    }

    public void setCurrent_service(String current_service) {
        this.current_service = current_service;
    }
}
