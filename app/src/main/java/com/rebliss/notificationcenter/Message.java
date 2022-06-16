package com.rebliss.notificationcenter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Message implements Serializable {

    @SerializedName("message")
   private String message;

    @SerializedName("push_message")
   private String pushMessage;

    @SerializedName("extra_data")
   private String extraData;

    @SerializedName("image_url")
    private String iageUr;

    @SerializedName("link")
    private String link;

    @SerializedName("created_at")
   private String createdAt;

    @SerializedName("notification_id")
   private Integer notificationId;

    public String getIageUr() {
        return iageUr;
    }

    public void setIageUr(String iageUr) {
        this.iageUr = iageUr;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }
}
