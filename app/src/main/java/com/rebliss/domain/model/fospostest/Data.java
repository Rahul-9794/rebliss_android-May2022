
package com.rebliss.domain.model.fospostest;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Data implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("auth_key")
    @Expose
    private String authKey;
    @SerializedName("password_hash")
    @Expose
    private String passwordHash;
    @SerializedName("password_reset_token")
    @Expose
    private Object passwordResetToken;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_detail_id")
    @Expose
    private String groupDetailId;
    @SerializedName("img_path")
    @Expose
    private String imgPath;
    @SerializedName("accept_tnc")
    @Expose
    private Object acceptTnc;
    @SerializedName("invite_user_id")
    @Expose
    private String inviteUserId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("group")
    @Expose
    private Object group;
    @SerializedName("email_verified")
    @Expose
    private String emailVerified;
    @SerializedName("sms_verified")
    @Expose
    private String smsVerified;
    @SerializedName("profile_verified")
    @Expose
    private String profileVerified;
    @SerializedName("device_type")
    @Expose
    private Object deviceType;
    @SerializedName("device_id")
    @Expose
    private Object deviceId;
    @SerializedName("last_login")
    @Expose
    private Object lastLogin;
    @SerializedName("by_admin")
    @Expose
    private String byAdmin;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("age_range")
    @Expose
    private String ageRange;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("location_zipcode")
    @Expose
    private Object locationZipcode;
    @SerializedName("location_city")
    @Expose
    private Object locationCity;
    @SerializedName("location_state")
    @Expose
    private Object locationState;
    @SerializedName("cp_type")
    @Expose
    private Object cpType;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("notification_count")
    @Expose
    private String notificationCount;
    @SerializedName("unique_ref_code")
    @Expose
    private String uniqueRefCode;
    @SerializedName("decline_message")
    @Expose
    private String declineMessage;
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("userDetail")
    @Expose
    private UserDetail userDetail;
    private final static long serialVersionUID = 3327128377756055174L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Object getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(Object passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupDetailId() {
        return groupDetailId;
    }

    public void setGroupDetailId(String groupDetailId) {
        this.groupDetailId = groupDetailId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Object getAcceptTnc() {
        return acceptTnc;
    }

    public void setAcceptTnc(Object acceptTnc) {
        this.acceptTnc = acceptTnc;
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getGroup() {
        return group;
    }

    public void setGroup(Object group) {
        this.group = group;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getSmsVerified() {
        return smsVerified;
    }

    public void setSmsVerified(String smsVerified) {
        this.smsVerified = smsVerified;
    }

    public String getProfileVerified() {
        return profileVerified;
    }

    public void setProfileVerified(String profileVerified) {
        this.profileVerified = profileVerified;
    }

    public Object getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Object deviceType) {
        this.deviceType = deviceType;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public Object getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Object lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getByAdmin() {
        return byAdmin;
    }

    public void setByAdmin(String byAdmin) {
        this.byAdmin = byAdmin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Object getLocationZipcode() {
        return locationZipcode;
    }

    public void setLocationZipcode(Object locationZipcode) {
        this.locationZipcode = locationZipcode;
    }

    public Object getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(Object locationCity) {
        this.locationCity = locationCity;
    }

    public Object getLocationState() {
        return locationState;
    }

    public void setLocationState(Object locationState) {
        this.locationState = locationState;
    }

    public Object getCpType() {
        return cpType;
    }

    public void setCpType(Object cpType) {
        this.cpType = cpType;
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

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
    }

    public String getUniqueRefCode() {
        return uniqueRefCode;
    }

    public void setUniqueRefCode(String uniqueRefCode) {
        this.uniqueRefCode = uniqueRefCode;
    }

    public String getDeclineMessage() {
        return declineMessage;
    }

    public void setDeclineMessage(String declineMessage) {
        this.declineMessage = declineMessage;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

}
