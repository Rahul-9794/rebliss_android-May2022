
package com.rebliss.domain.model.fospostest;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetail implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("bday")
    @Expose
    private Object bday;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("cellphone")
    @Expose
    private Object cellphone;
    @SerializedName("web_page")
    @Expose
    private Object webPage;
    @SerializedName("gst_no")
    @Expose
    private String gstNo;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("landline_no")
    @Expose
    private String landlineNo;
    @SerializedName("personal_email_id")
    @Expose
    private String personalEmailId;
    @SerializedName("official_email_id")
    @Expose
    private String officialEmailId;
    @SerializedName("cp_firm_name")
    @Expose
    private String cpFirmName;
    @SerializedName("communication_address_id")
    @Expose
    private String communicationAddressId;
    @SerializedName("bussiness_address_id")
    @Expose
    private String bussinessAddressId;
    @SerializedName("bank_holder_name")
    @Expose
    private String bankHolderName;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("ifsc_code")
    @Expose
    private String ifscCode;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("upload_type_option")
    @Expose
    private String uploadTypeOption;
    @SerializedName("aadhar_no")
    @Expose
    private String aadharNo;
    @SerializedName("company_pan_no")
    @Expose
    private String companyPanNo;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("pos_father_name")
    @Expose
    private String posFatherName;
    @SerializedName("driving_license_no")
    @Expose
    private Object drivingLicenseNo;
    @SerializedName("em_contact_person_name")
    @Expose
    private String emContactPersonName;
    @SerializedName("em_person_contact_no")
    @Expose
    private String emPersonContactNo;
    @SerializedName("partner_id")
    @Expose
    private String partnerId;
    @SerializedName("id_proof")
    @Expose
    private Object idProof;
    @SerializedName("cp_adhar_proof")
    @Expose
    private String cpAdharProof;
    @SerializedName("cp_pan_proof")
    @Expose
    private String cpPanProof;
    @SerializedName("firm_pan_proof")
    @Expose
    private Object firmPanProof;
    @SerializedName("gst_proof")
    @Expose
    private Object gstProof;
    @SerializedName("address_proof")
    @Expose
    private Object addressProof;
    @SerializedName("business_type")
    @Expose
    private String businessType;
    @SerializedName("manpower_strength")
    @Expose
    private String manpowerStrength;
    @SerializedName("business_locations")
    @Expose
    private Object businessLocations;
    @SerializedName("turnover_business")
    @Expose
    private Object turnoverBusiness;
    @SerializedName("nature_of_business")
    @Expose
    private String natureOfBusiness;
    @SerializedName("cheque_proof")
    @Expose
    private String chequeProof;
    @SerializedName("driving_license_proof")
    @Expose
    private Object drivingLicenseProof;
    @SerializedName("passport_size_photo")
    @Expose
    private String passportSizePhoto;
    @SerializedName("signed_form")
    @Expose
    private Object signedForm;
    @SerializedName("shop_photo")
    @Expose
    private Object shopPhoto;
    @SerializedName("ib_da_code")
    @Expose
    private String ibDaCode;
    @SerializedName("ib_da_proof")
    @Expose
    private Object ibDaProof;
    @SerializedName("shop_latitude")
    @Expose
    private Object shopLatitude;
    @SerializedName("shop_longitude")
    @Expose
    private Object shopLongitude;
    @SerializedName("industry_type_other")
    @Expose
    private Object industryTypeOther;
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    private final static long serialVersionUID = 1048887025894522520L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public Object getBday() {
        return bday;
    }

    public void setBday(Object bday) {
        this.bday = bday;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Object getCellphone() {
        return cellphone;
    }

    public void setCellphone(Object cellphone) {
        this.cellphone = cellphone;
    }

    public Object getWebPage() {
        return webPage;
    }

    public void setWebPage(Object webPage) {
        this.webPage = webPage;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLandlineNo() {
        return landlineNo;
    }

    public void setLandlineNo(String landlineNo) {
        this.landlineNo = landlineNo;
    }

    public String getPersonalEmailId() {
        return personalEmailId;
    }

    public void setPersonalEmailId(String personalEmailId) {
        this.personalEmailId = personalEmailId;
    }

    public String getOfficialEmailId() {
        return officialEmailId;
    }

    public void setOfficialEmailId(String officialEmailId) {
        this.officialEmailId = officialEmailId;
    }

    public String getCpFirmName() {
        return cpFirmName;
    }

    public void setCpFirmName(String cpFirmName) {
        this.cpFirmName = cpFirmName;
    }

    public String getCommunicationAddressId() {
        return communicationAddressId;
    }

    public void setCommunicationAddressId(String communicationAddressId) {
        this.communicationAddressId = communicationAddressId;
    }

    public String getBussinessAddressId() {
        return bussinessAddressId;
    }

    public void setBussinessAddressId(String bussinessAddressId) {
        this.bussinessAddressId = bussinessAddressId;
    }

    public String getBankHolderName() {
        return bankHolderName;
    }

    public void setBankHolderName(String bankHolderName) {
        this.bankHolderName = bankHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUploadTypeOption() {
        return uploadTypeOption;
    }

    public void setUploadTypeOption(String uploadTypeOption) {
        this.uploadTypeOption = uploadTypeOption;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getCompanyPanNo() {
        return companyPanNo;
    }

    public void setCompanyPanNo(String companyPanNo) {
        this.companyPanNo = companyPanNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPosFatherName() {
        return posFatherName;
    }

    public void setPosFatherName(String posFatherName) {
        this.posFatherName = posFatherName;
    }

    public Object getDrivingLicenseNo() {
        return drivingLicenseNo;
    }

    public void setDrivingLicenseNo(Object drivingLicenseNo) {
        this.drivingLicenseNo = drivingLicenseNo;
    }

    public String getEmContactPersonName() {
        return emContactPersonName;
    }

    public void setEmContactPersonName(String emContactPersonName) {
        this.emContactPersonName = emContactPersonName;
    }

    public String getEmPersonContactNo() {
        return emPersonContactNo;
    }

    public void setEmPersonContactNo(String emPersonContactNo) {
        this.emPersonContactNo = emPersonContactNo;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public Object getIdProof() {
        return idProof;
    }

    public void setIdProof(Object idProof) {
        this.idProof = idProof;
    }

    public String getCpAdharProof() {
        return cpAdharProof;
    }

    public void setCpAdharProof(String cpAdharProof) {
        this.cpAdharProof = cpAdharProof;
    }

    public String getCpPanProof() {
        return cpPanProof;
    }

    public void setCpPanProof(String cpPanProof) {
        this.cpPanProof = cpPanProof;
    }

    public Object getFirmPanProof() {
        return firmPanProof;
    }

    public void setFirmPanProof(Object firmPanProof) {
        this.firmPanProof = firmPanProof;
    }

    public Object getGstProof() {
        return gstProof;
    }

    public void setGstProof(Object gstProof) {
        this.gstProof = gstProof;
    }

    public Object getAddressProof() {
        return addressProof;
    }

    public void setAddressProof(Object addressProof) {
        this.addressProof = addressProof;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getManpowerStrength() {
        return manpowerStrength;
    }

    public void setManpowerStrength(String manpowerStrength) {
        this.manpowerStrength = manpowerStrength;
    }

    public Object getBusinessLocations() {
        return businessLocations;
    }

    public void setBusinessLocations(Object businessLocations) {
        this.businessLocations = businessLocations;
    }

    public Object getTurnoverBusiness() {
        return turnoverBusiness;
    }

    public void setTurnoverBusiness(Object turnoverBusiness) {
        this.turnoverBusiness = turnoverBusiness;
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public String getChequeProof() {
        return chequeProof;
    }

    public void setChequeProof(String chequeProof) {
        this.chequeProof = chequeProof;
    }

    public Object getDrivingLicenseProof() {
        return drivingLicenseProof;
    }

    public void setDrivingLicenseProof(Object drivingLicenseProof) {
        this.drivingLicenseProof = drivingLicenseProof;
    }

    public String getPassportSizePhoto() {
        return passportSizePhoto;
    }

    public void setPassportSizePhoto(String passportSizePhoto) {
        this.passportSizePhoto = passportSizePhoto;
    }

    public Object getSignedForm() {
        return signedForm;
    }

    public void setSignedForm(Object signedForm) {
        this.signedForm = signedForm;
    }

    public Object getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(Object shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public String getIbDaCode() {
        return ibDaCode;
    }

    public void setIbDaCode(String ibDaCode) {
        this.ibDaCode = ibDaCode;
    }

    public Object getIbDaProof() {
        return ibDaProof;
    }

    public void setIbDaProof(Object ibDaProof) {
        this.ibDaProof = ibDaProof;
    }

    public Object getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(Object shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public Object getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(Object shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public Object getIndustryTypeOther() {
        return industryTypeOther;
    }

    public void setIndustryTypeOther(Object industryTypeOther) {
        this.industryTypeOther = industryTypeOther;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
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

}
