package com.rebliss.domain.model.industrytype;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class IndustryType implements  Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;
    @SerializedName("text")
    private String text;
    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
