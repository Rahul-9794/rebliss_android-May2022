package com.rebliss.domain.model.profile;

import com.google.gson.annotations.SerializedName;
import com.rebliss.domain.model.editprofile.Communication;

import java.io.Serializable;

public class UserDetail implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("gender")
    private String gender;
    @SerializedName("address1")
    private String address;
    @SerializedName("photo")
    private String photo;
    @SerializedName("bday")
    private String bday;
    @SerializedName("location")
    private String location;
    @SerializedName("marital_status")
    private String marital_status;
    @SerializedName("cellphone")
    private String cellphone;
    @SerializedName("web_page")
    private String web_page;
    @SerializedName("gst_no")
    private String gst_no;
    @SerializedName("dob")
    private String dob;
    @SerializedName("landline_no")
    private String landline_no;
    @SerializedName("personal_email_id")
    private String personal_email_id;
    @SerializedName("official_email_id")
    private String official_email_id;
    @SerializedName("cp_firm_name")
    private String cp_firm_name;
    @SerializedName("communication_address_id")
    private String communication_address_id;
    @SerializedName("bussiness_address_id")
    private String bussiness_address_id;
    @SerializedName("bank_holder_name")
    private String bank_holder_name;
    @SerializedName("account_number")
    private String account_number;
    @SerializedName("ifsc_code")
    private String ifsc_code;
    @SerializedName("bank_name")
    private String bank_name;
    @SerializedName("aadhar_no")
    private String aadhar_no;
    @SerializedName("company_pan_no")
    private String company_pan_no;
    @SerializedName("pan_no")
    private String pan_no;
    @SerializedName("pos_father_name")
    private String pos_father_name;
    @SerializedName("driving_license_no")
    private String driving_license_no;
    @SerializedName("em_contact_person_name")
    private String em_contact_person_name;
    @SerializedName("em_person_contact_no")
    private String em_person_contact_no;
    @SerializedName("partner_id")
    private String partner_id;
    @SerializedName("id_proof")
    private String id_proof;
    @SerializedName("created")
    private String created;
    @SerializedName("business_type")
    private String business_type;
    @SerializedName("manpower_strength")
    private String manpower_strength;
    @SerializedName("business_locations")
    private String business_locations;
    @SerializedName("upload_type_option")
    private int upload_type_option;
    @SerializedName("nature_of_business")
    private String nature_of_business;

    @SerializedName("shop_latitude")
    private String shop_latitude;

    public String getShop_latitude() {
        return shop_latitude;
    }

    public void setShop_latitude(String shop_latitude) {
        this.shop_latitude = shop_latitude;
    }


    public String getShop_longitude() {
        return shop_longitude;
    }

    public void setShop_longitude(String shop_longitude) {
        this.shop_longitude = shop_longitude;
    }

    @SerializedName("shop_longitude")
    private String shop_longitude;


    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getManpower_strength() {
        return manpower_strength;
    }

    public void setManpower_strength(String manpower_strength) {
        this.manpower_strength = manpower_strength;
    }

    public String getBusiness_locations() {
        return business_locations;
    }

    public void setBusiness_locations(String business_locations) {
        this.business_locations = business_locations;
    }

    public String getTurnover_business() {
        return turnover_business;
    }

    public void setTurnover_business(String turnover_business) {
        this.turnover_business = turnover_business;
    }

    @SerializedName("turnover_business")
    private String turnover_business;

    public String getIndustry_type_other() {
        return industry_type_other;
    }

    public void setIndustry_type_other(String industry_type_other) {
        this.industry_type_other = industry_type_other;
    }

    @SerializedName("industry_type_other")
    private String industry_type_other;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getWeb_page() {
        return web_page;
    }

    public void setWeb_page(String web_page) {
        this.web_page = web_page;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLandline_no() {
        return landline_no;
    }

    public void setLandline_no(String landline_no) {
        this.landline_no = landline_no;
    }

    public String getPersonal_email_id() {
        return personal_email_id;
    }

    public void setPersonal_email_id(String personal_email_id) {
        this.personal_email_id = personal_email_id;
    }

    public String getOfficial_email_id() {
        return official_email_id;
    }

    public void setOfficial_email_id(String official_email_id) {
        this.official_email_id = official_email_id;
    }

    public String getCp_firm_name() {
        return cp_firm_name;
    }

    public void setCp_firm_name(String cp_firm_name) {
        this.cp_firm_name = cp_firm_name;
    }

    public String getCommunication_address_id() {
        return communication_address_id;
    }

    public void setCommunication_address_id(String communication_address_id) {
        this.communication_address_id = communication_address_id;
    }

    public String getBussiness_address_id() {
        return bussiness_address_id;
    }

    public void setBussiness_address_id(String bussiness_address_id) {
        this.bussiness_address_id = bussiness_address_id;
    }

    public String getBank_holder_name() {
        return bank_holder_name;
    }

    public void setBank_holder_name(String bank_holder_name) {
        this.bank_holder_name = bank_holder_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public int getUpload_type_option() {
        return upload_type_option;
    }

    public void setUpload_type_option(int upload_type_option) {
        this.upload_type_option = upload_type_option;
    }

    public String getCompany_pan_no() {
        return company_pan_no;
    }

    public void setCompany_pan_no(String company_pan_no) {
        this.company_pan_no = company_pan_no;
    }

    public String getPan_no() {
        return pan_no;
    }

    public void setPan_no(String pan_no) {
        this.pan_no = pan_no;
    }

    public String getPos_father_name() {
        return pos_father_name;
    }

    public void setPos_father_name(String pos_father_name) {
        this.pos_father_name = pos_father_name;
    }

    public String getDriving_license_no() {
        return driving_license_no;
    }

    public void setDriving_license_no(String driving_license_no) {
        this.driving_license_no = driving_license_no;
    }

    public String getEm_contact_person_name() {
        return em_contact_person_name;
    }

    public void setEm_contact_person_name(String em_contact_person_name) {
        this.em_contact_person_name = em_contact_person_name;
    }

    public String getEm_person_contact_no() {
        return em_person_contact_no;
    }

    public void setEm_person_contact_no(String em_person_contact_no) {
        this.em_person_contact_no = em_person_contact_no;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getId_proof() {
        return id_proof;
    }

    public void setId_proof(String id_proof) {
        this.id_proof = id_proof;
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

    public String getNature_of_business() {
        return nature_of_business;
    }

    public void setNature_of_business(String nature_of_business) {
        this.nature_of_business = nature_of_business;
    }

    @SerializedName("modified")
    private String modified;

    public Communication getCommunication() {
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    @SerializedName("communication")
    private Communication communication;



}
