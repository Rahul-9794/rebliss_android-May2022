package com.rebliss.domain.model.profile;

import com.google.gson.annotations.SerializedName;
import com.rebliss.domain.model.editprofile.Business;
import com.rebliss.domain.model.editprofile.Communication;
import com.rebliss.domain.model.editprofile.Partner;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Data implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String first_name;
    @SerializedName("is_parent")
    private String is_parent;

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
    @SerializedName("modified")
    private String modified;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("gender")
    private String gender;
    @SerializedName("address")
    private String address;
    @SerializedName("ib_da_code")
    private String ib_da_code;

    @SerializedName("occupation")
    private String occupation;

    @SerializedName("fos_type")
    private String fos_type;


    @SerializedName("age_range")
    private String ageRange;

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @SerializedName("education")
    private String education;

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
    @SerializedName("cp_adhar_proof")
    private String cp_adhar_proof;
    @SerializedName("cp_pan_proof")
    private String cp_pan_proof;
    @SerializedName("firm_pan_proof")
    private String firm_pan_proof;
    @SerializedName("gst_proof")
    private String gst_proof;
    @SerializedName("passport_size_photo")
    private String passport_size_photo;
    @SerializedName("signed_form")
    private String signed_form;
    @SerializedName("upload_type_option")
    private int upload_type_option;

    @SerializedName("shop_latitude")
    private String shop_latitude;

    @SerializedName("fos_shop_name")
    private String fos_shop_name;

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

    public String getUnique_ref_code() {
        return unique_ref_code;
    }

    public void setUnique_ref_code(String unique_ref_code) {
        this.unique_ref_code = unique_ref_code;
    }

    @SerializedName("unique_ref_code")
    private String unique_ref_code;

    public String getNature_of_business() {
        return nature_of_business;
    }

    public void setNature_of_business(String nature_of_business) {
        this.nature_of_business = nature_of_business;
    }

    @SerializedName("nature_of_business")
    private String nature_of_business;

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

    public String getCp_adhar_proof() {
        return cp_adhar_proof;
    }

    public void setCp_adhar_proof(String cp_adhar_proof) {
        this.cp_adhar_proof = cp_adhar_proof;
    }

    public int getUpload_type_option() {
        return upload_type_option;
    }

    public void setUpload_type_option(int upload_type_option) {
        this.upload_type_option = upload_type_option;
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

    @SerializedName("business_type")
    private String business_type;

    public String getDecline_message() {
        return decline_message;
    }

    public void setDecline_message(String decline_message) {
        this.decline_message = decline_message;
    }

    @SerializedName("decline_message")
    private String decline_message;
    @SerializedName("manpower_strength")
    private String manpower_strength;
    @SerializedName("business_locations")
    private String business_locations;

    public String getTurnover_business() {
        return turnover_business;
    }

    public void setTurnover_business(String turnover_business) {
        this.turnover_business = turnover_business;
    }

    @SerializedName("turnover_business")
    private String turnover_business;

    public String getUpload_path() {
        return upload_path;
    }

    public void setUpload_path(String upload_path) {
        this.upload_path = upload_path;
    }

    @SerializedName("upload_path")
    private String upload_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Business getBussiness() {
        return bussiness;
    }

    public void setBussiness(Business bussiness) {
        this.bussiness = bussiness;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Communication getCommunication() {
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    @SerializedName("bussiness")
    private Business bussiness;

    @SerializedName("partner")
    private Partner partner;
    @SerializedName("communication")
    private Communication communication;

    public String getIndustry_type_other() {
        return industry_type_other;
    }

    public void setIndustry_type_other(String industry_type_other) {
        this.industry_type_other = industry_type_other;
    }

    @SerializedName("industry_type_other")
    private String industry_type_other;

    public String getIs_parent() {
        return is_parent;
    }

    public void setIs_parent(String is_parent) {
        this.is_parent = is_parent;
    }

    public String getFos_type() {
        return fos_type;
    }

    public void setFos_type(String fos_type) {
        this.fos_type = fos_type;
    }

    public String getFos_shop_name() {
        return fos_shop_name;
    }

    public void setFos_shop_name(String fos_shop_name) {
        this.fos_shop_name = fos_shop_name;
    }
}
