package com.rebliss.notificationcenter;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ExtraData implements Serializable {
    @SerializedName("user_challenge_profile_id")
    private int user_challenge_profile_id;
    @SerializedName("eventId")
    private int eventId;
    @SerializedName("badge")
    private int badge;
    @SerializedName("user_type")
    private int user_type;

    public int getUser_challenge_profile_id() {
        return user_challenge_profile_id;
    }

    public void setUser_challenge_profile_id(int user_challenge_profile_id) {
        this.user_challenge_profile_id = user_challenge_profile_id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @SerializedName("eventName")
    private String eventName;
}
