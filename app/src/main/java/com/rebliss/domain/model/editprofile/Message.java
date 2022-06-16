package com.rebliss.domain.model.editprofile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    @SerializedName("success")
    private List<String> success;

    public List<String> getSuccess() {
        return success;
    }

    public void setSuccess(List<String> success) {
        this.success = success;
    }

    public List<String> getPersonal_email_id() {
        return personal_email_id;
    }

    public void setPersonal_email_id(List<String> personal_email_id) {
        this.personal_email_id = personal_email_id;
    }

    @SerializedName("personal_email_id")
    private List<String> personal_email_id;

    @SerializedName("aadhar_no")
    private List<String> aadhar_no;



    public List<String> getOfficial_email_id() {
        return official_email_id;
    }

    public void setOfficial_email_id(List<String> official_email_id) {
        this.official_email_id = official_email_id;
    }

    @SerializedName("official_email_id")
    private List<String> official_email_id;

    public List<String> getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(List<String> aadhar_no) {
        this.aadhar_no = aadhar_no;
    }
}
