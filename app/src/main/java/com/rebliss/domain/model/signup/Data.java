package com.rebliss.domain.model.signup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    @SerializedName("error_message")
    private List<String> error_message;
    @SerializedName("message")
    private String message;

    public List<String> getError_message() {
        return error_message;
    }

    public void setError_message(List<String> error_message) {
        this.error_message = error_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("token")
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @SerializedName("user")
    private User user;

}
