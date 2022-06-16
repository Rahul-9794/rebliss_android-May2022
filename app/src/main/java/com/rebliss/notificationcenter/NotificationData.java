package com.rebliss.notificationcenter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public  class NotificationData implements Serializable{

   @SerializedName("extraData")
    private ExtraData extraData;

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @SerializedName("message")
    private Message message;
}

