package com.rebliss.domain.model.fileupload;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FileUploadResponce implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private String data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    @SerializedName("file_name")
    private String file_name;

}
