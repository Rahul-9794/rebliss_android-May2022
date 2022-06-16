package com.rebliss.domain.model.editprofile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EditProfileRequest implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("phone_number")
    private String phone_number;
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
    @SerializedName("bank_holder_name")
    private String bank_holder_name;
    @SerializedName("account_number")
    private String account_number;
    @SerializedName("ifsc_code")
    private String ifsc_code;
    @SerializedName("gst_no")
    private String gst_no;
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
    @SerializedName("gender")
    private String gender;
    @SerializedName("marital_status")
    private String marital_status;
    @SerializedName("driving_license_no")
    private String driving_license_no;

    @SerializedName("em_contact_person_name")
    private String em_contact_person_name;
    @SerializedName("em_person_contact_no")
    private String em_person_contact_no;
    @SerializedName("id_proof")
    private String id_proof;
    @SerializedName("id_proof_file_type")
    private String id_proof_file_type;
    @SerializedName("business_type")
    private String business_type;
    @SerializedName("manpower_strength")
    private String manpower_strength;
    @SerializedName("business_locations")
    private String business_locations;
    @SerializedName("upload_type_option")
    private int upload_type_option;

    @SerializedName("shop_latitude")
    private String shop_latitude;
    @SerializedName("ib_da_code")
    private String ib_da_code;

    public String getIb_da_code() {
        return ib_da_code;
    }

    public void setIb_da_code(String ib_da_code) {
        this.ib_da_code = ib_da_code;
    }

    public String getIb_da_proof() {
        return ib_da_proof;
    }

    public void setIb_da_proof(String ib_da_proof) {
        this.ib_da_proof = ib_da_proof;
    }

    @SerializedName("ib_da_proof")
    private String ib_da_proof;

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

    public String getIndustry_type_other() {
        return industry_type_other;
    }

    public void setIndustry_type_other(String industry_type_other) {
        this.industry_type_other = industry_type_other;
    }

    @SerializedName("industry_type_other")
    private String industry_type_other;

    @SerializedName("shop_longitude")
    private String shop_longitude;

    public String getNature_of_business() {
        return nature_of_business;
    }

    public void setNature_of_business(String nature_of_business) {
        this.nature_of_business = nature_of_business;
    }

    @SerializedName("nature_of_business")
    private String nature_of_business;

    public String getId_proof() {
        return id_proof;
    }

    public void setId_proof(String id_proof) {
        this.id_proof = id_proof;
    }

    public String getId_proof_file_type() {
        return id_proof_file_type;
    }

    public void setId_proof_file_type(String id_proof_file_type) {
        this.id_proof_file_type = id_proof_file_type;
    }

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
    @SerializedName("communication")
    private Communication communication;

    public Business getBussiness() {
        return bussiness;
    }

    public void setBussiness(Business bussiness) {
        this.bussiness = bussiness;
    }

    @SerializedName("bussiness")
    private Business bussiness;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
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

    public Communication getCommunication() {
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    @SerializedName("partner")
    private Partner partner;

    @SerializedName("cp_adhar_proof")
    private String cp_adhar_proof;

    public String getProfile_verified() {
        return profile_verified;
    }

    public void setProfile_verified(String profile_verified) {
        this.profile_verified = profile_verified;
    }

    @SerializedName("profile_verified")
    private String profile_verified;
    @SerializedName("cp_pan_proof")
    private String cp_pan_proof;
    @SerializedName("firm_pan_proof")
    private String firm_pan_proof;

    public String getCp_adhar_proof() {
        return cp_adhar_proof;
    }

    public void setCp_adhar_proof(String cp_adhar_proof) {
        this.cp_adhar_proof = cp_adhar_proof;
    }

    public String getCp_pan_proof() {
        return cp_pan_proof;
    }

    public void setCp_pan_proof(String cp_pan_proof) {
        this.cp_pan_proof = cp_pan_proof;
    }

    public String getFirm_pan_proof() {
        return firm_pan_proof;
    }

    public void setFirm_pan_proof(String firm_pan_proof) {
        this.firm_pan_proof = firm_pan_proof;
    }

    public String getGst_proof() {
        return gst_proof;
    }

    public void setGst_proof(String gst_proof) {
        this.gst_proof = gst_proof;
    }

    @SerializedName("gst_proof")
    private String gst_proof;

    public String getAddress_proof() {
        return address_proof;
    }

    public void setAddress_proof(String address_proof) {
        this.address_proof = address_proof;
    }

    @SerializedName("address_proof")
    private String address_proof;

    public String getCheque_proof() {
        return cheque_proof;
    }

    public void setCheque_proof(String cheque_proof) {
        this.cheque_proof = cheque_proof;
    }

    @SerializedName("cheque_proof")
    private String cheque_proof;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("address")
    private String address;
    @SerializedName("passport_size_photo")
    private String passport_size_photo;
    @SerializedName("signed_form")
    private String signed_form;

    public String getPassport_size_photo() {
        return passport_size_photo;
    }

    public void setPassport_size_photo(String passport_size_photo) {
        this.passport_size_photo = passport_size_photo;
    }

    public String getSigned_form() {
        return signed_form;
    }

    public void setSigned_form(String signed_form) {
        this.signed_form = signed_form;
    }

    public String getShop_photo() {
        return shop_photo;
    }

    public void setShop_photo(String shop_photo) {
        this.shop_photo = shop_photo;
    }

    @SerializedName("shop_photo")
    private String shop_photo;

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }
}
