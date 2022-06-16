package com.rebliss.domain.model.editprofile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Business implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("address1")
    private String address;
    @SerializedName("city")
    private String city;
    @SerializedName("address2")
    private String district;
    @SerializedName("state")
    private String state;
    @SerializedName("zipcode")
    private String pincode;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("created")
    private String created;
    @SerializedName("updated")
    private String updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getLand_mark() {
        return land_mark;
    }

    public void setLand_mark(String land_mark) {
        this.land_mark = land_mark;
    }

    @SerializedName("land_mark")
    private String land_mark;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("longitude")
    private String longitude;

}
